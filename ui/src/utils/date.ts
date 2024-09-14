import dayjs from "dayjs";
import "dayjs/locale/zh-cn";
import timezone from "dayjs/plugin/timezone";
import TimeAgo from "javascript-time-ago";
import zh from "javascript-time-ago/locale/zh";

dayjs.extend(timezone);

dayjs.locale("zh-cn");

TimeAgo.addDefaultLocale(zh);

export function formatDatetime(date: string | Date | undefined | null): string {
  if (!date) {
    return "";
  }
  return dayjs(date).format("YYYY-MM-DD HH:mm");
}

export function timeAgo(
  date: string | Date | number | undefined | null
): string {
  if (!date) {
    return "";
  }

  const currentDate = new Date();
  const inputDate = new Date(date);

  // 365天 * 24小时 * 60分钟 * 60秒 * 1000毫秒
  const oneYearInMilliseconds = 365 * 24 * 60 * 60 * 1000;

  if (currentDate.getTime() - inputDate.getTime() > oneYearInMilliseconds) {
    return dayjs(date).format("YYYY-MM-DD");
  }

  const timeAgo = new TimeAgo("zh");

  return timeAgo.format(new Date(date));
}


export function calculateTimeDifferenceInSeconds(
  startTimestamp: string | Date | number | undefined | null,
  completionTimestamp: string | Date | number | undefined | null
): string {
  if (!startTimestamp || !completionTimestamp) {
    return "Invalid input";
  }

  // 将输入转换为 Date 对象
  const startDate = new Date(startTimestamp);
  const completionDate = new Date(completionTimestamp);

  // 计算时间差（以毫秒为单位）
  const differenceInMilliseconds = completionDate.getTime() - startDate.getTime();

  // 将时间差转换为秒
  const differenceInSeconds = differenceInMilliseconds / 1000;

  // 返回时间差的整数部分
  return Math.floor(differenceInSeconds).toString();
}
