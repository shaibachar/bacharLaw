import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getFees } from 'app/entities/fee/fee.reducer';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntities as getAgreements } from 'app/entities/agreement/agreement.reducer';
import { createEntity, getEntity, reset, updateEntity } from './fee.reducer';

export const FeeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const fees = useAppSelector(state => state.fee.entities);
  const clients = useAppSelector(state => state.client.entities);
  const agreements = useAppSelector(state => state.agreement.entities);
  const feeEntity = useAppSelector(state => state.fee.entity);
  const loading = useAppSelector(state => state.fee.loading);
  const updating = useAppSelector(state => state.fee.updating);
  const updateSuccess = useAppSelector(state => state.fee.updateSuccess);

  const handleClose = () => {
    navigate(`/fee${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFees({}));
    dispatch(getClients({}));
    dispatch(getAgreements({}));
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.startDate = convertDateTimeToServer(values.startDate);
    if (values.value !== undefined && typeof values.value !== 'number') {
      values.value = Number(values.value);
    }

    const entity = {
      ...feeEntity,
      ...values,
      linkedFees: mapIdList(values.linkedFees),
      linkedTos: mapIdList(values.linkedTos),
      client: clients.find(it => it.id.toString() === values.client?.toString()),
      agreement: agreements.find(it => it.id.toString() === values.agreement?.toString()),
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
          startDate: displayDefaultDateTime(),
        }
      : {
          ...feeEntity,
          startDate: convertDateTimeFromServer(feeEntity.startDate),
          linkedFees: feeEntity?.linkedFees?.map(e => e.id.toString()),
          linkedTos: feeEntity?.linkedTos?.map(e => e.id.toString()),
          client: feeEntity?.client?.id,
          agreement: feeEntity?.agreement?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bacharLawApp.fee.home.createOrEditLabel" data-cy="FeeCreateUpdateHeading">
            <Translate contentKey="bacharLawApp.fee.home.createOrEditLabel">Create or edit a Fee</Translate>
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
                  id="fee-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bacharLawApp.fee.active')}
                id="fee-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('bacharLawApp.fee.adjustedValue')}
                id="fee-adjustedValue"
                name="adjustedValue"
                data-cy="adjustedValue"
                type="text"
              />
              <ValidatedField
                label={translate('bacharLawApp.fee.adjustedValuePlus')}
                id="fee-adjustedValuePlus"
                name="adjustedValuePlus"
                data-cy="adjustedValuePlus"
                type="text"
              />
              <ValidatedField label={translate('bacharLawApp.fee.amount')} id="fee-amount" name="amount" data-cy="amount" type="text" />
              <ValidatedField
                label={translate('bacharLawApp.fee.description')}
                id="fee-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('bacharLawApp.fee.name')} id="fee-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('bacharLawApp.fee.startDate')}
                id="fee-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('bacharLawApp.fee.value')} id="fee-value" name="value" data-cy="value" type="text" />
              <ValidatedField
                label={translate('bacharLawApp.fee.linkedFees')}
                id="fee-linkedFees"
                data-cy="linkedFees"
                type="select"
                multiple
                name="linkedFees"
              >
                <option value="" key="0" />
                {fees
                  ? fees.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('bacharLawApp.fee.linkedTo')}
                id="fee-linkedTo"
                data-cy="linkedTo"
                type="select"
                multiple
                name="linkedTos"
              >
                <option value="" key="0" />
                {fees
                  ? fees.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="fee-client" name="client" data-cy="client" label={translate('bacharLawApp.fee.client')} type="select">
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="fee-agreement"
                name="agreement"
                data-cy="agreement"
                label={translate('bacharLawApp.fee.agreement')}
                type="select"
              >
                <option value="" key="0" />
                {agreements
                  ? agreements.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/fee" replace color="info">
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

export default FeeUpdate;
