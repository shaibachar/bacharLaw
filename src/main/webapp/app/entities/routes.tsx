import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Client from './client';
import ClientHistory from './client-history';
import Fee from './fee';
import Payment from './payment';
import Agreement from './agreement';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="client/*" element={<Client />} />
        <Route path="client-history/*" element={<ClientHistory />} />
        <Route path="fee/*" element={<Fee />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="agreement/*" element={<Agreement />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
