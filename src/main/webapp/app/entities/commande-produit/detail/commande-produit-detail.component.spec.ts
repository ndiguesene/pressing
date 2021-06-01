import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommandeProduitDetailComponent } from './commande-produit-detail.component';

describe('Component Tests', () => {
  describe('CommandeProduit Management Detail Component', () => {
    let comp: CommandeProduitDetailComponent;
    let fixture: ComponentFixture<CommandeProduitDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CommandeProduitDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ commandeProduit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CommandeProduitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommandeProduitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load commandeProduit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.commandeProduit).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
