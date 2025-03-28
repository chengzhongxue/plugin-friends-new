<script lang="ts" setup>
import {VButton} from "@halo-dev/components";
import {computed, onMounted, ref, nextTick} from "vue";
import type {CronFriendPost} from "@/api/generated";
import cloneDeep from "lodash.clonedeep";
import {friendsCoreApiClient} from "@/api";
import { submitForm } from "@formkit/core";
import LinkFormKit from "@/components/formkit/LinkFormKit.vue";

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
    disableSyncList: undefined,
  },
  kind: "CronFriendPost",
  apiVersion: "friend.moony.la/v1alpha1",
};

const cronOptions = [{
  label: "每月（每月 1 号 0 点）",
  value: "@monthly"
}, {
  label: "每周（每周第一天 的 0 点）",
  value: "@weekly"
}, {
  label: "每天（每天的 0 点）",
  value: "@daily"
}, {
  label: "每小时",
  value: "@hourly"
}]

const isUpdateMode = computed(() => {
  return !!formState.value.metadata.creationTimestamp;
});

const  saving = ref(false);
const formState = ref<CronFriendPost>(cloneDeep(initialFormState));

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
          <FormKit
            type="checkbox"
            name="suspend"
            label="是否启用"
            value="false"
            help="定时获取RSS订阅数据"
          />
          <FormKit
            type="select"
            name="cron"
            label="定时表达式"
            allow-create
            searchable
            validatio="required"
            :options="cronOptions"
            help="定时表达式规则请参考：https://docs.spring.io/spring-framework/reference/integration/scheduling.html#scheduling-cron-expression"
          />
          <FormKit
            type="select"
            name="timezone"
            label="时区"
            :options="[
               {
                 value: 'Asia/Shanghai', 
                 label: 'Asia/Shanghai (GMT+08:00)'
               },
            ]"
          />
          <FormKit
            type="number"
            name="successfulRetainLimit"
            label="留限制条数"
            number="integer"
            validation="required|number|min:0"
            help="设置之后会保留的数据条数，设置为 0 即为5条"
          />
          <LinkFormKit
            name="disableSyncList"
            label="禁止同步名单"
          ></LinkFormKit>
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
