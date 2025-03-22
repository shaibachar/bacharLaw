import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';
import { PaymentOperation } from 'app/shared/model/enumerations/payment-operation.model';
import { PaymentType } from 'app/shared/model/enumerations/payment-type.model';

export interface IPayment {
  id?: number;
  done?: boolean | null;
  startDate?: dayjs.Dayjs | null;
  value?: number | null;
  operation?: keyof typeof PaymentOperation | null;
  paymentType?: keyof typeof PaymentType | null;
  client?: IClient | null;
}

export const defaultValue: Readonly<IPayment> = {
  done: false,
};
