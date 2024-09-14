<script lang="ts" setup>
import {calculateTimeDifferenceInSeconds, formatDatetime} from "@/utils/date";
import type { SyncFriendPost } from "@/api/generated";
import {
  VStatusDot,
  VEntity,
  VEntityField,
  VDropdownItem,
  VSpace,
  Dialog,
  Toast
} from "@halo-dev/components";
import {friendsCoreApiClient} from "@/api";
import { useQueryClient } from "@tanstack/vue-query";
import { toRefs } from "vue";

const queryClient = useQueryClient();

const props = withDefaults(
  defineProps<{
    syncFriendPost: SyncFriendPost;
    isSelected?: boolean;
  }>(),
  { isSelected: false }
);

const { syncFriendPost } = toRefs(props);

const handleDelete = async () => {
  Dialog.warning({
    title: "删除任务",
    description: "该操作会将任务删除，该操作不可恢复。",
    confirmType: "danger",
    confirmText: "确定",
    cancelText: "取消",
    onConfirm: async () => {
      try {
        await friendsCoreApiClient.syncFriendPost.deleteSyncFriendPost({
          name: props.syncFriendPost.metadata.name as string
        })

        Toast.success("删除成功");
      } catch (error) {
        console.error("Failed to delete syncFriendPost", error);
      } finally {
        await queryClient.invalidateQueries({ queryKey: ["syncFriendPosts"] });
      }
    },
  });
};


</script>
<template>
  <VEntity :is-selected="isSelected">
    <template #checkbox>
      <HasPermission :permissions="['plugin:friends:manage']">
        <slot name="checkbox" />
      </HasPermission>
    </template>

    <template #start>
      <VEntityField>
        <template #title>
          <div class="entity-field-title">
            {{ syncFriendPost.spec.linkDisplayName }}
          </div>
        </template>
        <template #description>
          <div class="flex flex-col gap-1.5">
            <VSpace class="flex-wrap !gap-y-1">
              <span class="text-xs text-gray-500">
                {{ syncFriendPost.spec.rss}}
              </span>
              <span class="text-xs text-gray-500" v-if="syncFriendPost.status.phase == 'SUCCEEDED'">
                {{ '同步文章：'+syncFriendPost.spec.syncCount+'篇'}}
              </span>
            </VSpace>
          </div>
        </template>
      </VEntityField>
    </template>
    <template #end>
      <VEntityField >
        <template #description>
          <VStatusDot
            v-if="syncFriendPost.status.phase == 'FAILED'"
            v-tooltip="syncFriendPost.status.failureMessage"
            state="error"
            animate
            :text="'同步失败'"
          />
          <VStatusDot
            v-if="syncFriendPost.status.phase == 'SUCCEEDED'"
            state="success"
            animate
            :text="'同步成功'"
          />
          <VStatusDot
            v-if="syncFriendPost.status.phase == 'RUNNING'"
            state="warning"
            animate
            :text="'同步中'"
          />
          <VStatusDot
            v-if="syncFriendPost.status.phase == 'PENDING'"
            state="warning"
            animate
            :text="'等待中'"
          />
        </template>
      </VEntityField>
      <VEntityField
        v-if="(syncFriendPost.status.completionTimestamp !=null && syncFriendPost.status.completionTimestamp != '') || syncFriendPost.status.phase == 'SUCCEEDED'"
        :description="'耗时 '+calculateTimeDifferenceInSeconds(syncFriendPost.status.startTimestamp,syncFriendPost.status.completionTimestamp)+'秒'"
        v-tooltip="'结束时间：'+formatDatetime(syncFriendPost.status.completionTimestamp)"
      ></VEntityField>
      <VEntityField 
        v-if="syncFriendPost.status.startTimestamp"
        v-tooltip="'开始同步时间'"
        :description="formatDatetime(syncFriendPost.status.startTimestamp) "
      ></VEntityField>
    </template>
    <template #dropdownItems>
      <HasPermission :permissions="['plugin:forums:posts:manage']">
        <VDropdownItem type="danger" @click="handleDelete">
          删除
        </VDropdownItem>
      </HasPermission>
    </template>
  </VEntity>
</template>
