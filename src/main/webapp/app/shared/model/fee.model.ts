import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';
import { IAgreement } from 'app/shared/model/agreement.model';

export interface IFee {
  id?: number;
  active?: boolean | null;
  adjustedValue?: string | null;
  adjustedValuePlus?: string | null;
  amount?: number | null;
  description?: string | null;
  name?: string | null;
  startDate?: dayjs.Dayjs | null;
  value?: number | null;
  linkedFees?: IFee[] | null;
  linkedTos?: IFee[] | null;
  client?: IClient | null;
  agreement?: IAgreement | null;
}

export const defaultValue: Readonly<IFee> = {
  active: false,
};
