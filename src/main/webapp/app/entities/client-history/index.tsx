import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ClientHistory from './client-history';
import ClientHistoryDetail from './client-history-detail';
import ClientHistoryUpdate from './client-history-update';
import ClientHistoryDeleteDialog from './client-history-delete-dialog';

const ClientHistoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ClientHistory />} />
    <Route path="new" element={<ClientHistoryUpdate />} />
    <Route path=":id">
      <Route index element={<ClientHistoryDetail />} />
      <Route path="edit" element={<ClientHistoryUpdate />} />
      <Route path="delete" element={<ClientHistoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ClientHistoryRoutes;
