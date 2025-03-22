import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment.reducer';

export const PaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentEntity = useAppSelector(state => state.payment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailsHeading">
          <Translate contentKey="bacharLawApp.payment.detail.title">Payment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.id}</dd>
          <dt>
            <span id="done">
              <Translate contentKey="bacharLawApp.payment.done">Done</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.done ? 'true' : 'false'}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="bacharLawApp.payment.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.startDate ? <TextFormat value={paymentEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="bacharLawApp.payment.value">Value</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.value}</dd>
          <dt>
            <span id="operation">
              <Translate contentKey="bacharLawApp.payment.operation">Operation</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.operation}</dd>
          <dt>
            <span id="paymentType">
              <Translate contentKey="bacharLawApp.payment.paymentType">Payment Type</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.paymentType}</dd>
          <dt>
            <Translate contentKey="bacharLawApp.payment.client">Client</Translate>
          </dt>
          <dd>{paymentEntity.client ? paymentEntity.client.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment/${paymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentDetail;
