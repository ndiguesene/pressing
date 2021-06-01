jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProduitService } from '../service/produit.service';
import { IProduit, Produit } from '../produit.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

import { ProduitUpdateComponent } from './produit-update.component';

describe('Component Tests', () => {
  describe('Produit Management Update Component', () => {
    let comp: ProduitUpdateComponent;
    let fixture: ComponentFixture<ProduitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let produitService: ProduitService;
    let categorieService: CategorieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProduitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProduitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProduitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      produitService = TestBed.inject(ProduitService);
      categorieService = TestBed.inject(CategorieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Categorie query and add missing value', () => {
        const produit: IProduit = { id: 456 };
        const categorie: ICategorie = { id: 6002 };
        produit.categorie = categorie;

        const categorieCollection: ICategorie[] = [{ id: 71013 }];
        spyOn(categorieService, 'query').and.returnValue(of(new HttpResponse({ body: categorieCollection })));
        const additionalCategories = [categorie];
        const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
        spyOn(categorieService, 'addCategorieToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        expect(categorieService.query).toHaveBeenCalled();
        expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(categorieCollection, ...additionalCategories);
        expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const produit: IProduit = { id: 456 };
        const categorie: ICategorie = { id: 13344 };
        produit.categorie = categorie;

        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(produit));
        expect(comp.categoriesSharedCollection).toContain(categorie);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const produit = { id: 123 };
        spyOn(produitService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: produit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(produitService.update).toHaveBeenCalledWith(produit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const produit = new Produit();
        spyOn(produitService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: produit }));
        saveSubject.complete();

        // THEN
        expect(produitService.create).toHaveBeenCalledWith(produit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const produit = { id: 123 };
        spyOn(produitService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(produitService.update).toHaveBeenCalledWith(produit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCategorieById', () => {
        it('Should return tracked Categorie primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategorieById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
