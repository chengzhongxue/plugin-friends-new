<script lang="ts" setup>
import type { PMNode } from "@halo-dev/richtext-editor";
import type { Editor, Node } from "@halo-dev/richtext-editor";
import { NodeViewWrapper } from "@halo-dev/richtext-editor";
import { computed, onMounted, ref, watch } from "vue";
import {formatDatetime} from "@/utils/date";
import { VButton,VSpace,VDropdown,VEmpty} from "@halo-dev/components";
import { friendsApiClient } from "@/api";
import type { RssDetail } from "@/api/generated";
import type { FriendsRss } from '@kunkunyu/fridends-rss';

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

const rssRef = ref<InstanceType<typeof FriendsRss> | null>();

watch(
  () => props.node.attrs.src,
  (value) => {
    if (value && rssRef.value) {
      rssRef.value.src = value;
      rssRef.value.fetchRssDetail();
    }
  }
);

const src = computed({
  get: () => {
    return props.node?.attrs.src;
  },
  set: (src: string) => {
    props.updateAttributes({ src: src });
  },
});

const layout = computed({
  get: () => {
    return props.node?.attrs.layout;
  },
  set: (layout: string) => {
    props.updateAttributes({ layout: layout });
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
  <node-view-wrapper as="div" class="contact-friends-rss-container"
      :class="{
        'contact-friends-rss-container--selected': selected,
      }">
    <div class="contact-friends-rss-nav">
      <div class="contact-friends-rss-nav-start">
        <div>RSS</div>
      </div>
      <div class="contact-friends-rss-nav-end">
        <button v-if="src && selecteRssDetail?.channel!=undefined"
                @click="handleResetInit"
                class="btn-sm btn-default btn" type="button">
          <span class="btn-content">更换</span>
        </button>
      </div>
    </div>
    <div class="contact-friends-rss-preview" >
      <VEmpty message="当前未输入RSS链接，点击下方按钮链接" title="未输入RSS链接"
              v-if="!src || selecteRssDetail?.channel==undefined">
        <template #actions>
          <VSpace>
            <VDropdown>
              <VButton>输入RSS链接</VButton>
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
        </template>
      </VEmpty>
      <div v-else inert="true" >
        <friends-rss
          ref="rssRef"
          :src="src"
          :layout="layout"
        ></friends-rss>
<!--        <ul class="block-rss" :class="{ 'is-grid columns-2': layout == 'grid' }">-->
<!--          <li class="block-rss__item" v-for="(item, index) in selecteRssDetail?.channel.items" :key="index">-->
<!--            <div class="block-rss__item-title"><a :href="item.link">{{item.title}}</a></div>-->
<!--          </li>-->
<!--        </ul>-->
      </div>
    </div>
  </node-view-wrapper>
</template>

<style>

.contact-friends-rss-container {
  --tw-ring-offset-shadow: var(--tw-ring-inset) 0 0 0 var(--tw-ring-offset-width) var(--tw-ring-offset-color);
  --tw-ring-shadow: var(--tw-ring-inset) 0 0 0 calc(1px + var(--tw-ring-offset-width)) var(--tw-ring-color);
  box-shadow: var(--tw-ring-offset-shadow),var(--tw-ring-shadow),var(--tw-shadow, 0 0 #0000);
  --tw-ring-opacity: 1;
  --tw-ring-color: rgb(229 231 235 / var(--tw-ring-opacity));
  border-radius: 4px;
  overflow: hidden;
  margin-top: .75em
}

.contact-friends-rss-container--selected {
  --tw-ring-offset-shadow: var(--tw-ring-inset) 0 0 0 var(--tw-ring-offset-width) var(--tw-ring-offset-color);
  --tw-ring-shadow: var(--tw-ring-inset) 0 0 0 calc(2px + var(--tw-ring-offset-width)) var(--tw-ring-color);
  box-shadow: var(--tw-ring-offset-shadow),var(--tw-ring-shadow),var(--tw-shadow, 0 0 #0000);
  --tw-ring-color: inherit
}

.contact-friends-rss-nav {
  border-bottom: 1px #e7e7e7 solid;
  display: flex;
  padding: 5px 10px;
  align-items: center
}

.contact-friends-rss-nav-start {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px
}

.contact-friends-rss-nav-end {
  justify-content: flex-end
}

.contact-friends-rss-preview {
  padding: 5px 10px
}
.halo-rich-text-editor .editor-content .ProseMirror .block-rss {
  list-style: none;
  padding: 0;
}
ul.block-rss.block-rss {
  box-sizing: border-box;
}
ul.block-rss.is-grid {
  display: flex;
  flex-wrap: wrap;
  list-style: none;
  padding: 0;
}

ul.block-rss.is-grid li {
  margin: 0 1em 1em 0;
  width: 100%
}

@media (min-width: 600px) {
  ul.block-rss.columns-2 li {
    width:calc(50% - 1em)
  }
}
.block-rss__item {
  display: flex;
  text-align: left;
}

</style>  
