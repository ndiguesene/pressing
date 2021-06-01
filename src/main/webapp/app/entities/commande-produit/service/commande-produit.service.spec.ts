import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { StatusCommandeProduit } from 'app/entities/enumerations/status-commande-produit.model';
import { ICommandeProduit, CommandeProduit } from '../commande-produit.model';

import { CommandeProduitService } from './commande-produit.service';

describe('Service Tests', () => {
  describe('CommandeProduit Service', () => {
    let service: CommandeProduitService;
    let httpMock: HttpTestingController;
    let elemDefault: ICommandeProduit;
    let expectedResult: ICommandeProduit | ICommandeProduit[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CommandeProduitService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        prixAvance: 0,
        prixRestant: 0,
        status: StatusCommandeProduit.PAYER,
        dateCommandeProduit: currentDate,
        dateLastModified: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCommandeProduit: currentDate.format(DATE_FORMAT),
            dateLastModified: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CommandeProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCommandeProduit: currentDate.format(DATE_FORMAT),
            dateLastModified: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCommandeProduit: currentDate,
            dateLastModified: currentDate,
          },
          returnedFromService
        );

        service.create(new CommandeProduit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CommandeProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prixAvance: 1,
            prixRestant: 1,
            status: 'BBBBBB',
            dateCommandeProduit: currentDate.format(DATE_FORMAT),
            dateLastModified: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCommandeProduit: currentDate,
            dateLastModified: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CommandeProduit', () => {
        const patchObject = Object.assign(
          {
            dateCommandeProduit: currentDate.format(DATE_FORMAT),
          },
          new CommandeProduit()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateCommandeProduit: currentDate,
            dateLastModified: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CommandeProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prixAvance: 1,
            prixRestant: 1,
            status: 'BBBBBB',
            dateCommandeProduit: currentDate.format(DATE_FORMAT),
            dateLastModified: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCommandeProduit: currentDate,
            dateLastModified: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CommandeProduit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCommandeProduitToCollectionIfMissing', () => {
        it('should add a CommandeProduit to an empty array', () => {
          const commandeProduit: ICommandeProduit = { id: 123 };
          expectedResult = service.addCommandeProduitToCollectionIfMissing([], commandeProduit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(commandeProduit);
        });

        it('should not add a CommandeProduit to an array that contains it', () => {
          const commandeProduit: ICommandeProduit = { id: 123 };
          const commandeProduitCollection: ICommandeProduit[] = [
            {
              ...commandeProduit,
            },
            { id: 456 },
          ];
          expectedResult = service.addCommandeProduitToCollectionIfMissing(commandeProduitCollection, commandeProduit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CommandeProduit to an array that doesn't contain it", () => {
          const commandeProduit: ICommandeProduit = { id: 123 };
          const commandeProduitCollection: ICommandeProduit[] = [{ id: 456 }];
          expectedResult = service.addCommandeProduitToCollectionIfMissing(commandeProduitCollection, commandeProduit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(commandeProduit);
        });

        it('should add only unique CommandeProduit to an array', () => {
          const commandeProduitArray: ICommandeProduit[] = [{ id: 123 }, { id: 456 }, { id: 10956 }];
          const commandeProduitCollection: ICommandeProduit[] = [{ id: 123 }];
          expectedResult = service.addCommandeProduitToCollectionIfMissing(commandeProduitCollection, ...commandeProduitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const commandeProduit: ICommandeProduit = { id: 123 };
          const commandeProduit2: ICommandeProduit = { id: 456 };
          expectedResult = service.addCommandeProduitToCollectionIfMissing([], commandeProduit, commandeProduit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(commandeProduit);
          expect(expectedResult).toContain(commandeProduit2);
        });

        it('should accept null and undefined values', () => {
          const commandeProduit: ICommandeProduit = { id: 123 };
          expectedResult = service.addCommandeProduitToCollectionIfMissing([], null, commandeProduit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(commandeProduit);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
