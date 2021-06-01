jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommandeProduitService } from '../service/commande-produit.service';
import { ICommandeProduit, CommandeProduit } from '../commande-produit.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { CommandeProduitUpdateComponent } from './commande-produit-update.component';

describe('Component Tests', () => {
  describe('CommandeProduit Management Update Component', () => {
    let comp: CommandeProduitUpdateComponent;
    let fixture: ComponentFixture<CommandeProduitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let commandeProduitService: CommandeProduitService;
    let produitService: ProduitService;
    let clientService: ClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommandeProduitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommandeProduitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommandeProduitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      commandeProduitService = TestBed.inject(CommandeProduitService);
      produitService = TestBed.inject(ProduitService);
      clientService = TestBed.inject(ClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Produit query and add missing value', () => {
        const commandeProduit: ICommandeProduit = { id: 456 };
        const produit: IProduit = { id: 27301 };
        commandeProduit.produit = produit;

        const produitCollection: IProduit[] = [{ id: 70855 }];
        spyOn(produitService, 'query').and.returnValue(of(new HttpResponse({ body: produitCollection })));
        const additionalProduits = [produit];
        const expectedCollection: IProduit[] = [...additionalProduits, ...produitCollection];
        spyOn(produitService, 'addProduitToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ commandeProduit });
        comp.ngOnInit();

        expect(produitService.query).toHaveBeenCalled();
        expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(produitCollection, ...additionalProduits);
        expect(comp.produitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Client query and add missing value', () => {
        const commandeProduit: ICommandeProduit = { id: 456 };
        const client: IClient = { id: 77579 };
        commandeProduit.client = client;

        const clientCollection: IClient[] = [{ id: 63140 }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ commandeProduit });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const commandeProduit: ICommandeProduit = { id: 456 };
        const produit: IProduit = { id: 1822 };
        commandeProduit.produit = produit;
        const client: IClient = { id: 65506 };
        commandeProduit.client = client;

        activatedRoute.data = of({ commandeProduit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(commandeProduit));
        expect(comp.produitsSharedCollection).toContain(produit);
        expect(comp.clientsSharedCollection).toContain(client);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const commandeProduit = { id: 123 };
        spyOn(commandeProduitService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ commandeProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: commandeProduit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(commandeProduitService.update).toHaveBeenCalledWith(commandeProduit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const commandeProduit = new CommandeProduit();
        spyOn(commandeProduitService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ commandeProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: commandeProduit }));
        saveSubject.complete();

        // THEN
        expect(commandeProduitService.create).toHaveBeenCalledWith(commandeProduit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const commandeProduit = { id: 123 };
        spyOn(commandeProduitService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ commandeProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(commandeProduitService.update).toHaveBeenCalledWith(commandeProduit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProduitById', () => {
        it('Should return tracked Produit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProduitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
