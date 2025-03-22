import client from 'app/entities/client/client.reducer';
import clientHistory from 'app/entities/client-history/client-history.reducer';
import fee from 'app/entities/fee/fee.reducer';
import payment from 'app/entities/payment/payment.reducer';
import agreement from 'app/entities/agreement/agreement.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  client,
  clientHistory,
  fee,
  payment,
  agreement,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
