/// <reference types="vite/client" />
/// <reference types="unplugin-icons/types/vue" />
export {};

declare module "axios" {
  export interface AxiosRequestConfig {
    mute?: boolean;
  }
}

declare module "vue" {
  interface ComponentCustomProperties {
    $formkit: any;
  }
}
