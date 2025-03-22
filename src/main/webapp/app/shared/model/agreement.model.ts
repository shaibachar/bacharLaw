import dayjs from 'dayjs';

export interface IAgreement {
  id?: number;
  name?: string | null;
  startDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IAgreement> = {};
