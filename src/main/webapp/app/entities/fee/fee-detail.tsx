import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './fee.reducer';

export const FeeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const feeEntity = useAppSelector(state => state.fee.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="feeDetailsHeading">
          <Translate contentKey="bacharLawApp.fee.detail.title">Fee</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{feeEntity.id}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="bacharLawApp.fee.active">Active</Translate>
            </span>
          </dt>
          <dd>{feeEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="adjustedValue">
              <Translate contentKey="bacharLawApp.fee.adjustedValue">Adjusted Value</Translate>
            </span>
          </dt>
          <dd>{feeEntity.adjustedValue}</dd>
          <dt>
            <span id="adjustedValuePlus">
              <Translate contentKey="bacharLawApp.fee.adjustedValuePlus">Adjusted Value Plus</Translate>
            </span>
          </dt>
          <dd>{feeEntity.adjustedValuePlus}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="bacharLawApp.fee.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{feeEntity.amount}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bacharLawApp.fee.description">Description</Translate>
            </span>
          </dt>
          <dd>{feeEntity.description}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bacharLawApp.fee.name">Name</Translate>
            </span>
          </dt>
          <dd>{feeEntity.name}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="bacharLawApp.fee.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{feeEntity.startDate ? <TextFormat value={feeEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="bacharLawApp.fee.value">Value</Translate>
            </span>
          </dt>
          <dd>{feeEntity.value}</dd>
          <dt>
            <Translate contentKey="bacharLawApp.fee.linkedFees">Linked Fees</Translate>
          </dt>
          <dd>
            {feeEntity.linkedFees
              ? feeEntity.linkedFees.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {feeEntity.linkedFees && i === feeEntity.linkedFees.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="bacharLawApp.fee.linkedTo">Linked To</Translate>
          </dt>
          <dd>
            {feeEntity.linkedTos
              ? feeEntity.linkedTos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {feeEntity.linkedTos && i === feeEntity.linkedTos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="bacharLawApp.fee.client">Client</Translate>
          </dt>
          <dd>{feeEntity.client ? feeEntity.client.id : ''}</dd>
          <dt>
            <Translate contentKey="bacharLawApp.fee.agreement">Agreement</Translate>
          </dt>
          <dd>{feeEntity.agreement ? feeEntity.agreement.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/fee" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/fee/${feeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FeeDetail;
