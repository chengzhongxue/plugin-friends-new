<script lang="ts" setup>
import type { SyncFriendPost } from "@/api/generated";
import { onMounted, ref, watch, provide } from "vue";
import type { Ref } from "vue";
import {
  VButton,
  VCard,
  VEmpty,
  VSpace,
  VLoading,
  VPagination,
  IconRefreshLine,
} from "@halo-dev/components";
import { useRouteQuery } from "@vueuse/router";
import { friendsCoreApiClient } from "@/api";
import SyncFriendPostListItem from "@/components/SyncFriendPostListItem.vue";
import { Dialog, Toast } from "@halo-dev/components";
import { useQuery } from "@tanstack/vue-query";


const selectedSyncFriendPostNames = ref<string[]>([]);
const checkedAll = ref(false);

const keyword = useRouteQuery<string>("keyword", "");
const page = useRouteQuery<number>("page", 1, {
  transform: Number,
});
const size = useRouteQuery<number>("size", 20, {
  transform: Number,
});
const total = ref(0);

provide<Ref<string[]>>("selectedSyncFriendPostNames", selectedSyncFriendPostNames);

watch(
  () => [
    keyword.value,
  ],
  () => {
    page.value = 1;
  },
);

const {
  data: syncFriendPosts,
  isLoading,
  isFetching,
  refetch,
} = useQuery({
  queryKey: [
    "syncFriendPosts",
    page,
    size,
    keyword,
  ],
  queryFn: async () => {
    
    const { data } = await friendsCoreApiClient.syncFriendPost.listSyncFriendPost({
      page: page.value,
      size: size.value,
    });

    total.value = data.total;
    return data.items;
  },
  refetchInterval: (data) => {

    const hasPhaseSyncFriendPost = data?.some((syncFriendPost) => {
      const { status } = syncFriendPost;
      return status.phase != 'SUCCEEDED'  &&  status.phase != 'FAILED'
    });

    if (hasPhaseSyncFriendPost) {
      return 1000;
    }
    
    const hasDeletingSyncFriendPost = data?.some(
      (syncFriendPost) => syncFriendPost?.metadata.deletionTimestamp,
    );
    return hasDeletingSyncFriendPost ? 1000 : false;
  },
});

const handleDeleteSyncFriendPostInBatch = async () => {
  Dialog.warning({
    title: "删除所选任务",
    description: "删除此任务之后。该操作不可恢复。",
    confirmType: "primary",
    confirmText: "确定",
    cancelText: "取消",
    onConfirm: async () => {
      try {
        const promises = selectedSyncFriendPostNames.value.map((name) => {
          return friendsCoreApiClient.syncFriendPost.deleteSyncFriendPost({
            name:name
          })
        })
        await Promise.all(promises);
        selectedSyncFriendPostNames.value = [];
        Toast.success("删除任务成功");
      } catch (e) {
        console.error("Failed to end syncFriendPost in batch", e);
      } finally {
        refetch();
      }
    },
  });
};

const checkSelection = (syncFriendPost: SyncFriendPost) => {
  if (syncFriendPost.metadata.name) {
    return selectedSyncFriendPostNames.value.includes(syncFriendPost.metadata.name);
  }
  return false;
};

const handleCheckAllChange = () => {

  if (checkedAll.value) {
    selectedSyncFriendPostNames.value = syncFriendPosts.value ?.map((syncFriendPost) => syncFriendPost.metadata.name) || [];
  } else {
    selectedSyncFriendPostNames.value = [];
  }
};


watch(selectedSyncFriendPostNames.value, (newVal) => {
  checkedAll.value = newVal.length === syncFriendPosts.value?.length;
});
</script>
<template>
  <VCard :body-class="['!p-0']">
    <template #header>
      <div class="block w-full bg-gray-50 px-4 py-3">
        <div
          class="relative flex h-9 flex-col flex-wrap items-start gap-4 sm:flex-row sm:items-center"
        >
          <HasPermission :permissions="['plugin:friends:manage']">
            <div class="hidden items-center sm:flex">
              <input
                v-model="checkedAll"
                type="checkbox"
                @change="handleCheckAllChange"
              />
            </div>
          </HasPermission>
          <div class="flex w-full flex-1 items-center sm:w-auto">
            <VSpace v-if="selectedSyncFriendPostNames.length > 0">
              <VButton type="danger" @click="handleDeleteSyncFriendPostInBatch">
                删除
              </VButton>
            </VSpace>
          </div>
          <VSpace spacing="lg" class="flex-wrap">
            <div class="flex flex-row gap-2">
              <div
                class="group cursor-pointer rounded p-1 hover:bg-gray-200"
                @click="refetch()"
              >
                <IconRefreshLine
                  v-tooltip="'刷新'"
                  :class="{ 'animate-spin text-gray-900': isFetching }"
                  class="h-4 w-4 text-gray-600 group-hover:text-gray-900"
                />
              </div>
            </div>
          </VSpace>
        </div>
      </div>
    </template>
    <VLoading v-if="isLoading" />
    <Transition v-else-if="!syncFriendPosts?.length" appear name="fade">
      <VEmpty
        :title="'当前没有日志'"
      >
        <template #actions>
          <VSpace>
            <VButton @click="refetch">
              刷新
            </VButton>
          </VSpace>
        </template>
      </VEmpty>
    </Transition>

    <Transition appear name="fade">
      <ul
        class="box-border h-full w-full divide-y divide-gray-100"
        role="list"
      >
        <li v-for="(syncFriendPost, index) in syncFriendPosts" :key="index">
          <SyncFriendPostListItem
            :sync-friend-post="syncFriendPost"
            :is-selected="checkSelection(syncFriendPost)"
          >
            <template #checkbox>
              <input
                v-model="selectedSyncFriendPostNames"
                :value="syncFriendPost.metadata.name"
                type="checkbox"
              />
            </template>
          </SyncFriendPostListItem>
        </li>
      </ul>
    </Transition>
    <template #footer>
      <VPagination
        v-model:page="page"
        v-model:size="size"
        page-label="页"
        size-label="条 / 页"
        :total-label="`共 ${total} 项数据`"
        :total="total"
        :size-options="[20, 30, 50, 100]"
      />
    </template>
  </VCard>
</template>
