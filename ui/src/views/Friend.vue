<script setup lang="ts">
import {markRaw, shallowRef} from "vue";
import FriendPostListItem from "@/views/FriendPostListItem.vue";
import Cron from "@/views/Cron.vue";
import { useRouteQuery } from "@vueuse/router";
import { Dialog, Toast } from "@halo-dev/components";

import {
  VButton,
  VCard,
  VPageHeader,
  VSpace,
  VTabbar,
} from "@halo-dev/components";
import {axiosInstance} from "@halo-dev/api-client";
import SyncFriendPost from "@/views/SyncFriendPost.vue";

const tabs = shallowRef([
  {
    id: "friendPost",
    label: "订阅文章",
    component: markRaw(FriendPostListItem),
  },
  {
    id: "cron",
    label: "定时任务",
    component: markRaw(Cron),
  },
  {
    id: "syncFriendPost",
    label: "同步日志",
    component: markRaw(SyncFriendPost),
  }
  
]);

const activeIndex = useRouteQuery<string>("tab", tabs.value[0].id);

//新增导出
const handleCreate = () => {
  Dialog.warning({
    title: "同步RSS数据",
    description: "点击按钮后，后台将进行同步RSS数据。",
    confirmType: "danger",
    confirmText: "确定",
    cancelText: "取消",
    onConfirm: async () => {
      try {
        await axiosInstance.post("/apis/api.plugin.halo.run/v1alpha1/plugins/plugin-friends/friendPost/synchronizationFriend")
          .then((res: any) => {
            Toast.success("同步RSS数据成功");
          });
      } catch (e) {
        console.error("", e);
      }
    },
  });
}
</script>

<template>

  <VPageHeader title="RSS订阅">
    <template #actions>
      <VSpace v-permission="['plugin:friends:manage']">
        <VButton type="secondary" @click="handleCreate">
          同步数据
        </VButton>
      </VSpace>
    </template>

  </VPageHeader>

  <div class="m-0 md:m-4">
    <VCard :body-class="['!p-0']">
      <template #header>
        <VTabbar
          v-model:active-id="activeIndex"
          :items="tabs.map((item) => ({ id: item.id, label: item.label }))"
          class="w-full !rounded-none"
          type="outline"
        ></VTabbar>
      </template>
      <div class="bg-white">
        <FriendPostListItem ref="friendPost" v-if="activeIndex=='friendPost'"/>
        <Cron ref="cron" v-if="activeIndex=='cron'"/>
        <SyncFriendPost ref="syncFriendPost" v-if="activeIndex=='syncFriendPost'"/>
      </div>
    </VCard>
  </div>


</template>

<style lang="scss" scoped>

#plugin-export-anything {
  height: 100vh;
  background-color: #f8fafc;
}

.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  gap: 1.5rem;

  .title {
    font-weight: 700;
    font-size: 1.25rem;
    line-height: 1.75rem;
  }

  .message {
    font-size: 0.875rem;
    line-height: 1.25rem;
    color: #4b5563;
  }

  .docs {
    display: grid;
    grid-template-columns: repeat(1, minmax(0, 1fr));
    gap: 1rem;
    max-width: 48rem;

    .docs__box {
      background-color: #fff;
      border-radius: 0.375rem;
      padding: 0.75rem;
      transition-property: all;
      transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
      transition-duration: 300ms;
      cursor: pointer;
      filter: drop-shadow(0 1px 2px rgb(0 0 0 / 0.1)) drop-shadow(0 1px 1px rgb(0 0 0 / 0.06));

      &:hover {
        box-shadow: 0 0 0 0px #fff, 0 0 0 1px rgb(59 130 246 / 0.5), 0 0 #0000;
      }

      .docs__box-title {
        display: flex;
        flex-direction: row;
        font-size: 1.125rem;
        line-height: 1.75rem;
        font-weight: 700;
        margin-bottom: 2rem;
        gap: 0.5rem;
        align-items: center;
      }

      .docs__box-message {
        font-size: 0.875rem;
        line-height: 1.25rem;
        color: #4b5563;
      }

      .docs__box-arrow {
        pointer-events: none;
        position: absolute;
        top: 1rem;
        right: 1rem;
        transition-property: all;
        transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
        transition-duration: 150ms;
        color: #d1d5db;
      }

      &:hover {
        .docs__box-arrow {
          color: #9ca3af;
          transform: translate(00.375rem, 0) rotate(0) skewX(0) skewY(0) scaleX(1) scaleY(1);
        }
      }
    }
  }

  @media (min-width: 640px) {
    .docs {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
  }
}
</style>
