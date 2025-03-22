import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Fee from './fee';
import FeeDetail from './fee-detail';
import FeeUpdate from './fee-update';
import FeeDeleteDialog from './fee-delete-dialog';

const FeeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Fee />} />
    <Route path="new" element={<FeeUpdate />} />
    <Route path=":id">
      <Route index element={<FeeDetail />} />
      <Route path="edit" element={<FeeUpdate />} />
      <Route path="delete" element={<FeeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FeeRoutes;
