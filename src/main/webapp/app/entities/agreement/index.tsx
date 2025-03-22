import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Agreement from './agreement';
import AgreementDetail from './agreement-detail';
import AgreementUpdate from './agreement-update';
import AgreementDeleteDialog from './agreement-delete-dialog';

const AgreementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Agreement />} />
    <Route path="new" element={<AgreementUpdate />} />
    <Route path=":id">
      <Route index element={<AgreementDetail />} />
      <Route path="edit" element={<AgreementUpdate />} />
      <Route path="delete" element={<AgreementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgreementRoutes;
