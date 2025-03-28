import { axiosInstance } from "@halo-dev/api-client";
import {
  ApiFriendMoonyLaV1alpha1FriendPostApi,
  CronFriendPostV1alpha1Api,
  FriendPostV1alpha1Api,
  RssFeedSyncLogV1alpha1Api

  
} from "./generated";

const friendsCoreApiClient = {
  cron: new CronFriendPostV1alpha1Api(undefined, "", axiosInstance),
  friendPost: new FriendPostV1alpha1Api(undefined, "", axiosInstance),
  rssFeedSyncLog: new RssFeedSyncLogV1alpha1Api(undefined, "", axiosInstance)
  
};

const friendsApiClient = {
  friendPost: new ApiFriendMoonyLaV1alpha1FriendPostApi(undefined,"",axiosInstance),
};
export { friendsCoreApiClient, friendsApiClient };
