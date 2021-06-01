import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommandeProduit, getCommandeProduitIdentifier } from '../commande-produit.model';

export type EntityResponseType = HttpResponse<ICommandeProduit>;
export type EntityArrayResponseType = HttpResponse<ICommandeProduit[]>;

@Injectable({ providedIn: 'root' })
export class CommandeProduitService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/commande-produits');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(commandeProduit: ICommandeProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commandeProduit);
    return this.http
      .post<ICommandeProduit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(commandeProduit: ICommandeProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commandeProduit);
    return this.http
      .put<ICommandeProduit>(`${this.resourceUrl}/${getCommandeProduitIdentifier(commandeProduit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(commandeProduit: ICommandeProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commandeProduit);
    return this.http
      .patch<ICommandeProduit>(`${this.resourceUrl}/${getCommandeProduitIdentifier(commandeProduit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommandeProduit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommandeProduit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommandeProduitToCollectionIfMissing(
    commandeProduitCollection: ICommandeProduit[],
    ...commandeProduitsToCheck: (ICommandeProduit | null | undefined)[]
  ): ICommandeProduit[] {
    const commandeProduits: ICommandeProduit[] = commandeProduitsToCheck.filter(isPresent);
    if (commandeProduits.length > 0) {
      const commandeProduitCollectionIdentifiers = commandeProduitCollection.map(
        commandeProduitItem => getCommandeProduitIdentifier(commandeProduitItem)!
      );
      const commandeProduitsToAdd = commandeProduits.filter(commandeProduitItem => {
        const commandeProduitIdentifier = getCommandeProduitIdentifier(commandeProduitItem);
        if (commandeProduitIdentifier == null || commandeProduitCollectionIdentifiers.includes(commandeProduitIdentifier)) {
          return false;
        }
        commandeProduitCollectionIdentifiers.push(commandeProduitIdentifier);
        return true;
      });
      return [...commandeProduitsToAdd, ...commandeProduitCollection];
    }
    return commandeProduitCollection;
  }

  protected convertDateFromClient(commandeProduit: ICommandeProduit): ICommandeProduit {
    return Object.assign({}, commandeProduit, {
      dateCommandeProduit: commandeProduit.dateCommandeProduit?.isValid()
        ? commandeProduit.dateCommandeProduit.format(DATE_FORMAT)
        : undefined,
      dateLastModified: commandeProduit.dateLastModified?.isValid() ? commandeProduit.dateLastModified.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCommandeProduit = res.body.dateCommandeProduit ? dayjs(res.body.dateCommandeProduit) : undefined;
      res.body.dateLastModified = res.body.dateLastModified ? dayjs(res.body.dateLastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((commandeProduit: ICommandeProduit) => {
        commandeProduit.dateCommandeProduit = commandeProduit.dateCommandeProduit ? dayjs(commandeProduit.dateCommandeProduit) : undefined;
        commandeProduit.dateLastModified = commandeProduit.dateLastModified ? dayjs(commandeProduit.dateLastModified) : undefined;
      });
    }
    return res;
  }
}
