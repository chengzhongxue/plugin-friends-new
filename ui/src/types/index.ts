export interface Metadata {
  name: string;
  generateName?: string;
  labels?: {
    [key: string]: string;
  } | null;
  annotations?: {
    [key: string]: string;
  } | null;
  version?: number | null;
  creationTimestamp?: string | null;
  deletionTimestamp?: string | null;
}

export interface Link {

  'apiVersion': string;

  'kind': string;

  'metadata': Metadata;

  'spec'?: LinkSpec;
}

export interface LinkSpec {

  'description'?: string;

  'displayName': string;

  'groupName'?: string;

  'logo'?: string;

  'priority'?: number;

  'url': string;
}

export interface LinkList {

  'first': boolean;

  'hasNext': boolean;

  'hasPrevious': boolean;

  'items': Array<Link>;

  'last': boolean;

  'page': number;

  'size': number;

  'total': number;

  'totalPages': number;
}

