import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agreement.reducer';

export const AgreementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const agreementEntity = useAppSelector(state => state.agreement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agreementDetailsHeading">
          <Translate contentKey="bacharLawApp.agreement.detail.title">Agreement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{agreementEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bacharLawApp.agreement.name">Name</Translate>
            </span>
          </dt>
          <dd>{agreementEntity.name}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="bacharLawApp.agreement.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {agreementEntity.startDate ? <TextFormat value={agreementEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/agreement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/agreement/${agreementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgreementDetail;
