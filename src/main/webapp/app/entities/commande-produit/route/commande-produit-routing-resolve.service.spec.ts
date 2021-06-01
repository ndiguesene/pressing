jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICommandeProduit, CommandeProduit } from '../commande-produit.model';
import { CommandeProduitService } from '../service/commande-produit.service';

import { CommandeProduitRoutingResolveService } from './commande-produit-routing-resolve.service';

describe('Service Tests', () => {
  describe('CommandeProduit routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CommandeProduitRoutingResolveService;
    let service: CommandeProduitService;
    let resultCommandeProduit: ICommandeProduit | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CommandeProduitRoutingResolveService);
      service = TestBed.inject(CommandeProduitService);
      resultCommandeProduit = undefined;
    });

    describe('resolve', () => {
      it('should return ICommandeProduit returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommandeProduit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommandeProduit).toEqual({ id: 123 });
      });

      it('should return new ICommandeProduit if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommandeProduit = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCommandeProduit).toEqual(new CommandeProduit());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommandeProduit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommandeProduit).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
