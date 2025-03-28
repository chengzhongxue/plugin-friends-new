import type {Link} from "@/api/generated";

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

