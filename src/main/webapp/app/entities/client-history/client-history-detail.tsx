import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './client-history.reducer';

export const ClientHistoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const clientHistoryEntity = useAppSelector(state => state.clientHistory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientHistoryDetailsHeading">
          <Translate contentKey="bacharLawApp.clientHistory.detail.title">ClientHistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clientHistoryEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bacharLawApp.clientHistory.description">Description</Translate>
            </span>
          </dt>
          <dd>{clientHistoryEntity.description}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="bacharLawApp.clientHistory.date">Date</Translate>
            </span>
          </dt>
          <dd>{clientHistoryEntity.date ? <TextFormat value={clientHistoryEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="bacharLawApp.clientHistory.type">Type</Translate>
            </span>
          </dt>
          <dd>{clientHistoryEntity.type}</dd>
          <dt>
            <span id="subType">
              <Translate contentKey="bacharLawApp.clientHistory.subType">Sub Type</Translate>
            </span>
          </dt>
          <dd>{clientHistoryEntity.subType}</dd>
          <dt>
            <Translate contentKey="bacharLawApp.clientHistory.client">Client</Translate>
          </dt>
          <dd>{clientHistoryEntity.client ? clientHistoryEntity.client.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/client-history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client-history/${clientHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClientHistoryDetail;
