<script lang="ts" setup>
import {VButton} from "@halo-dev/components";
import {computed, onMounted, ref, nextTick} from "vue";
import type {CronFriendPost} from "@/api/generated";
import cloneDeep from "lodash.clonedeep";
import {friendsCoreApiClient} from "@/api";
import { submitForm } from "@formkit/core";

const Se = "cron-friends-default"

const initialFormState: CronFriendPost = {
  metadata: {
    name: Se,
    creationTimestamp: ""
  },
  spec: {
    cron: "0 0 * * * *",
    timezone:"Asia/Shanghai",
    suspend: false,
    successfulRetainLimit: 0,
  },
  kind: "CronFriendPost",
  apiVersion: "friend.moony.la/v1alpha1",
};


const isUpdateMode = computed(() => {
  return !!formState.value.metadata.creationTimestamp;
});

const  saving = ref(false);
const formState = ref<CronFriendPost>(cloneDeep(initialFormState));
const formSchema = ref(
  [

    {
      $formkit: 'checkbox',
      name: 'suspend',
      label: '是否启用',
      value: false,
      help: '定时获取RSS订阅数据'
    },
    {
      $cmp: 'FormKit',
      props: {
        type: 'text',
        name: 'cron',
        label: '定时表达式',
        validation: 'required',
        help: '定时任务表达式，请参考文档'
      }
    },
    {
      $cmp: 'FormKit',
      props: {
        type: 'select',
        name: 'timezone',
        label: '时区',
        options: [
          {value: "Asia/Shanghai", label: 'Asia/Shanghai (GMT+08:00)'},
        ],
      }
    },
    {
      $formkit: 'number',
      name: 'successfulRetainLimit',
      label: '留限制条数',
      help: '设置之后会保留的数据条数，设置为 0 即为5条',
      number: "integer",
      validation: 'required|number|min:0',
    },
  ]
)

const mutate = async () => {
  saving.value = true;
  try {
    if (isUpdateMode.value) {
      const {
        data: data
      } = await friendsCoreApiClient.cron.getCronFriendPost({
        name: Se
      });
      return formState.value = {
        ...formState.value,
        status: data.status,
        metadata: data.metadata
      },
      await friendsCoreApiClient.cron.updateCronFriendPost({
        name: Se,
        cronFriendPost: formState.value
      });
    } else {
      const { data: createCronFriendPost } = await friendsCoreApiClient.cron.createCronFriendPost({
        cronFriendPost: formState.value
      });
      formState.value = createCronFriendPost
    }
  } finally {
    saving.value = false;
  }
}

onMounted(async () => {

  const {data: data} = await friendsCoreApiClient.cron.listCronFriendPost();
  const items = data.items;
  if (items?.length){
    formState.value = items[0]
  }
  
});
const handleSave = () => {

  nextTick(() => {
    submitForm("cron-setting");
  });
};

</script>

<template>
  <Transition mode="out-in" name="fade">
    <div class="bg-white p-4">
      <div>
        <FormKit
          id="cron-setting"
          v-model="formState.spec"
          name="cron-setting"
          :actions="false"
          :preserve="true"
          type="form"
          @submit="mutate"
          submit-label="Login"
        >
          <FormKitSchema :schema="formSchema"/>
        </FormKit>
      </div>
      <div v-permission="['plugin:friends:manage']" class="pt-5">
        <div class="flex justify-start">
          <VButton
            :loading="saving"
            type="secondary"
            @click="handleSave"
          >
            保存
          </VButton>
        </div>
      </div>
    </div>
  </Transition>
</template>
