<script lang="ts" setup>
import { setFocus } from "@/utils/focus";
import {
  axiosInstance
} from "@halo-dev/api-client";
import {
  IconArrowDown,
  VAvatar,
  VDropdown,
  VEntity,
  VEntityField,
} from "@halo-dev/components";
import { useQuery } from "@tanstack/vue-query";
import { refDebounced } from "@vueuse/shared";
import { ref, toRefs } from "vue";
import type { Link, LinkList } from "@/types";

const props = withDefaults(
  defineProps<{
    label: string;
    modelValue?: string;
  }>(),
  {
    modelValue: undefined,
  }
);

const { modelValue } = toRefs(props);

const emit = defineEmits<{
  (event: "update:modelValue", value?: string): void;
}>();

const keyword = ref("");
const debouncedKeyword = refDebounced(keyword, 300);

const { data: selectedLink } = useQuery({
  queryKey: ["core:link:name", modelValue],
  queryFn: async () => {
    if (!modelValue.value) {
      return null;
    }

    const { data } = await axiosInstance.get(`/apis/core.halo.run/v1alpha1/links/${modelValue.value}`);

    return data;
  },
  cacheTime: 0,
});

const { data: links } = useQuery({
  queryKey: ["core:links", debouncedKeyword],
  queryFn: async () => {
    const { data } = await axiosInstance.get<LinkList>(
      "/apis/api.plugin.halo.run/v1alpha1/plugins/PluginLinks/links",
      {
        params: {
          page: 1,
          size: 30,
          keyword: debouncedKeyword.value
        },
      }
    );

    const pureLinks = data?.items?.map((link) => link);

    if (!pureLinks?.length) {
      return [selectedLink.value].filter(Boolean) as Link[];
    }

    if (selectedLink.value) {
      return [
        selectedLink.value,
        ...pureLinks.filter(
          (link) => link.metadata.name !== selectedLink.value?.metadata.name
        ),
      ];
    }

    return pureLinks;
  },
  staleTime: 2000,
});

const dropdown = ref();

const handleSelect = (link: Link) => {
  const { name } = link.metadata || {};

  if (name === props.modelValue) {
    emit("update:modelValue", undefined);
  } else {
    emit("update:modelValue", name);
  }

  dropdown.value.hide();
};

function onDropdownShow() {
  setTimeout(() => {
    setFocus("linkFilterDropdownInput");
  }, 200);
}
</script>

<template>
  <VDropdown ref="dropdown" :classes="['!p-0']" @show="onDropdownShow">
    <div
      class="flex cursor-pointer select-none items-center text-sm text-gray-700 hover:text-black"
      :class="{ 'font-semibold text-gray-700': modelValue !== undefined }"
    >
      <span v-if="!selectedLink" class="mr-0.5">
        {{ label }}
      </span>
      <span v-else class="mr-0.5">
        {{ label }}：{{ selectedLink.spec.displayName }}
      </span>
      <span>
        <IconArrowDown />
      </span>
    </div>
    <template #popper>
      <div class="h-96 w-80">
        <div class="border-b border-b-gray-100 bg-white p-4">
          <FormKit
            id="linkFilterDropdownInput"
            v-model="keyword"
            :placeholder="'输入关键词以搜索'"
            type="text"
          ></FormKit>
        </div>
        <div>
          <ul
            class="box-border h-full w-full divide-y divide-gray-100"
            role="list"
          >
            <li
              v-for="link in links"
              :key="link.metadata.name"
              class="cursor-pointer"
              @click="handleSelect(link)"
            >
              <VEntity :is-selected="modelValue === link.metadata.name">
                <template #start>
                  <VEntityField>
                    <template #description>
                      <VAvatar
                        :key="link.metadata.name"
                        :alt="link.spec.displayName"
                        :src="link.spec.logo"
                        size="md"
                      ></VAvatar>
                    </template>
                  </VEntityField>
                  <VEntityField
                    :title="link.spec.displayName"
                    :description="link.metadata.name"
                  />
                </template>
              </VEntity>
            </li>
          </ul>
        </div>
      </div>
    </template>
  </VDropdown>
</template>
