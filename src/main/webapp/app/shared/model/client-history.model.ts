import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';
import { ClientHistoryType } from 'app/shared/model/enumerations/client-history-type.model';
import { ClientHistorySubType } from 'app/shared/model/enumerations/client-history-sub-type.model';

export interface IClientHistory {
  id?: number;
  description?: string | null;
  date?: dayjs.Dayjs | null;
  type?: keyof typeof ClientHistoryType | null;
  subType?: keyof typeof ClientHistorySubType | null;
  client?: IClient | null;
}

export const defaultValue: Readonly<IClientHistory> = {};
