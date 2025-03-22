import dayjs from 'dayjs';

export interface IClient {
  id?: number;
  address?: string | null;
  description?: string | null;
  email?: string | null;
  fullName?: string | null;
  lastUpdated?: dayjs.Dayjs | null;
  phone?: string | null;
  active?: boolean | null;
}

export const defaultValue: Readonly<IClient> = {
  active: false,
};
