<script lang="ts" setup>
import type { PMNode } from "@halo-dev/richtext-editor";
import type { Editor, Node } from "@halo-dev/richtext-editor";
import { NodeViewWrapper } from "@halo-dev/richtext-editor";
import { computed, onMounted, ref } from "vue";
import {formatDatetime} from "@/utils/date";
import { VButton,VSpace,VDropdown} from "@halo-dev/components";
import { friendsApiClient } from "@/api";
import type { RssDetail } from "@/api/generated";

const selecteRssDetail = ref<RssDetail | undefined>();

const props = defineProps<{
  editor: Editor;
  node: PMNode;
  selected: boolean;
  extension: Node<any, any>;
  getPos: () => number;
  updateAttributes: (attributes: Record<string, any>) => void;
  deleteNode: () => void;
}>();

const src = computed({
  get: () => {
    return props.node?.attrs.src;
  },
  set: (src: string) => {
    props.updateAttributes({ src: src });
  },
});


const editorLinkObtain = ref();

onMounted(() => {
  if (!src.value) {
  }else {
    handleCheckAllChange();
  }
});

const handleCheckAllChange = async () => {
  const { data: data } = await friendsApiClient.friendPost.parsingRss({
    rssUrl: src.value
  });
  selecteRssDetail.value = data
};

const handleEnterSetExternalLink = () => {
  if (!editorLinkObtain.value) {
    return;
  }
  props.updateAttributes({
    src: editorLinkObtain.value,
  });
};

const handleResetInit = () => {
  props.updateAttributes({src: ""});
};

</script>

<template>
  <node-view-wrapper as="div" class="inline-block-box inline-block">
    <div
      class="inline-block overflow-hidden rounded-md transition-all text-center h-full w-full"
      :class="{
        'rounded ring-2': selected,
      }"
    >
      <div v-if="!src || selecteRssDetail?.channel==undefined" class="relative">
        <div class="flex h-64 w-full items-center justify-center" style="height: 150px;">
          <div
            class="flex h-full w-full cursor-pointer flex-col border-2 border-dashed border-gray-300 bg-gray-50"
          >
            <div
              class="flex flex-col items-center justify-center space-y-7 pb-6 pt-5 editor-link-obtain"
            >
              <VSpace>
                <VDropdown>
                  <div class="flex h-14 w-14 items-center justify-center rounded-full bg-primary/20" style="margin: 0.8em 1.5em;">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24" aria-hidden="true" focusable="false"><path d="M5 10.2h-.8v1.5H5c1.9 0 3.8.8 5.1 2.1 1.4 1.4 2.1 3.2 2.1 5.1v.8h1.5V19c0-2.3-.9-4.5-2.6-6.2-1.6-1.6-3.8-2.6-6.1-2.6zm10.4-1.6C12.6 5.8 8.9 4.2 5 4.2h-.8v1.5H5c3.5 0 6.9 1.4 9.4 3.9s3.9 5.8 3.9 9.4v.8h1.5V19c0-3.9-1.6-7.6-4.4-10.4zM4 20h3v-3H4v3z"></path></svg>
                  </div>
                  <VButton>输入链接</VButton>
                  <template #popper>
                    <input
                      v-model="editorLinkObtain"
                      class="block w-full rounded-md border border-gray-300 bg-gray-50 px-2 py-1.5 text-sm text-gray-900 hover:bg-gray-100"
                      placeholder="输入链接，按回车确定"
                      @keydown.enter="handleEnterSetExternalLink"
                      @change="handleCheckAllChange"
                    />
                  </template>
                </VDropdown>
              </VSpace>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="group relative">
        <div inert="true">
          <ul class="block-rss">
            <li class="block-rss__item" v-for="(item, index) in selecteRssDetail?.channel.items" :key="index">
              <div class="block-rss__item-title"><a :href="item.link">{{item.title}}</a></div>
            </li>
          </ul>
        </div>
        <div
          v-if="src"
          class="absolute left-0 top-0 hidden h-1/4 w-full cursor-pointer justify-end bg-gradient-to-b from-gray-300 to-transparent p-2 ease-in-out group-hover:flex"
        >
          <VButton size="sm" type="secondary" @click="handleResetInit">
            替换
          </VButton>
        </div>
      </div>
    </div>
  </node-view-wrapper>
</template>

<style>

:root {
  --db--text-color-light: rgba(0, 0, 0, 0.6);
}

.inline-block-box {
  width: calc(100% - 1px);
}
.editor-link-obtain .v-popper--theme-dropdown {
  margin-top: -10px;
}
.halo-rich-text-editor .editor-content .ProseMirror .block-rss {
  list-style: none;
  padding: 0;
}
ul.block-rss.block-rss {
  box-sizing: border-box;
}
.block-rss__item {
  display: flex;
  text-align: left;
}
</style>  
