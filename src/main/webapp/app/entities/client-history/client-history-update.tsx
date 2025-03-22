import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { ClientHistoryType } from 'app/shared/model/enumerations/client-history-type.model';
import { ClientHistorySubType } from 'app/shared/model/enumerations/client-history-sub-type.model';
import { createEntity, getEntity, reset, updateEntity } from './client-history.reducer';

export const ClientHistoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clients = useAppSelector(state => state.client.entities);
  const clientHistoryEntity = useAppSelector(state => state.clientHistory.entity);
  const loading = useAppSelector(state => state.clientHistory.loading);
  const updating = useAppSelector(state => state.clientHistory.updating);
  const updateSuccess = useAppSelector(state => state.clientHistory.updateSuccess);
  const clientHistoryTypeValues = Object.keys(ClientHistoryType);
  const clientHistorySubTypeValues = Object.keys(ClientHistorySubType);

  const handleClose = () => {
    navigate(`/client-history${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClients({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...clientHistoryEntity,
      ...values,
      client: clients.find(it => it.id.toString() === values.client?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          type: 'PAYMENT',
          subType: 'CREATE',
          ...clientHistoryEntity,
          date: convertDateTimeFromServer(clientHistoryEntity.date),
          client: clientHistoryEntity?.client?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bacharLawApp.clientHistory.home.createOrEditLabel" data-cy="ClientHistoryCreateUpdateHeading">
            <Translate contentKey="bacharLawApp.clientHistory.home.createOrEditLabel">Create or edit a ClientHistory</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="client-history-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bacharLawApp.clientHistory.description')}
                id="client-history-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('bacharLawApp.clientHistory.date')}
                id="client-history-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('bacharLawApp.clientHistory.type')}
                id="client-history-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {clientHistoryTypeValues.map(clientHistoryType => (
                  <option value={clientHistoryType} key={clientHistoryType}>
                    {translate(`bacharLawApp.ClientHistoryType.${clientHistoryType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('bacharLawApp.clientHistory.subType')}
                id="client-history-subType"
                name="subType"
                data-cy="subType"
                type="select"
              >
                {clientHistorySubTypeValues.map(clientHistorySubType => (
                  <option value={clientHistorySubType} key={clientHistorySubType}>
                    {translate(`bacharLawApp.ClientHistorySubType.${clientHistorySubType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="client-history-client"
                name="client"
                data-cy="client"
                label={translate('bacharLawApp.clientHistory.client')}
                type="select"
              >
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/client-history" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ClientHistoryUpdate;
