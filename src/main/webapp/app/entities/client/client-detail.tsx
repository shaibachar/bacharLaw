import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './client.reducer';

export const ClientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const clientEntity = useAppSelector(state => state.client.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientDetailsHeading">
          <Translate contentKey="bacharLawApp.client.detail.title">Client</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clientEntity.id}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="bacharLawApp.client.address">Address</Translate>
            </span>
          </dt>
          <dd>{clientEntity.address}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bacharLawApp.client.description">Description</Translate>
            </span>
          </dt>
          <dd>{clientEntity.description}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="bacharLawApp.client.email">Email</Translate>
            </span>
          </dt>
          <dd>{clientEntity.email}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="bacharLawApp.client.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{clientEntity.fullName}</dd>
          <dt>
            <span id="lastUpdated">
              <Translate contentKey="bacharLawApp.client.lastUpdated">Last Updated</Translate>
            </span>
          </dt>
          <dd>{clientEntity.lastUpdated ? <TextFormat value={clientEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="bacharLawApp.client.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{clientEntity.phone}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="bacharLawApp.client.active">Active</Translate>
            </span>
          </dt>
          <dd>{clientEntity.active ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/client" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client/${clientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClientDetail;
