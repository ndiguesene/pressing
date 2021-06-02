import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommandeProduit, CommandeProduit } from '../commande-produit.model';
import { CommandeProduitService } from '../service/commande-produit.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-commande-produit-update',
  templateUrl: './commande-produit-update.component.html',
})
export class CommandeProduitUpdateComponent implements OnInit {
  isSaving = false;

  produitsSharedCollection: IProduit[] = [];
  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    prixAvance: [],
    prixRestant: [],
    status: [],
    dateCommandeProduit: [],
    dateLastModified: [],
    produits: [],
    client: [],
  });

  constructor(
    protected commandeProduitService: CommandeProduitService,
    protected produitService: ProduitService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandeProduit }) => {
      this.updateForm(commandeProduit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commandeProduit = this.createFromForm();
    if (commandeProduit.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeProduitService.update(commandeProduit));
    } else {
      this.subscribeToSaveResponse(this.commandeProduitService.create(commandeProduit));
    }
  }

  trackProduitById(index: number, item: IProduit): number {
    return item.id!;
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  getSelectedProduit(option: IProduit, selectedVals?: IProduit[]): IProduit {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommandeProduit>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(commandeProduit: ICommandeProduit): void {
    this.editForm.patchValue({
      id: commandeProduit.id,
      prixAvance: commandeProduit.prixAvance,
      prixRestant: commandeProduit.prixRestant,
      status: commandeProduit.status,
      dateCommandeProduit: commandeProduit.dateCommandeProduit,
      dateLastModified: commandeProduit.dateLastModified,
      produits: commandeProduit.produits,
      client: commandeProduit.client,
    });

    this.produitsSharedCollection = this.produitService.addProduitToCollectionIfMissing(
      this.produitsSharedCollection,
      ...(commandeProduit.produits ?? [])
    );
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, commandeProduit.client);
  }

  protected loadRelationshipsOptions(): void {
    this.produitService
      .query()
      .pipe(map((res: HttpResponse<IProduit[]>) => res.body ?? []))
      .pipe(
        map((produits: IProduit[]) =>
          this.produitService.addProduitToCollectionIfMissing(produits, ...(this.editForm.get('produits')!.value ?? []))
        )
      )
      .subscribe((produits: IProduit[]) => (this.produitsSharedCollection = produits));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): ICommandeProduit {
    return {
      ...new CommandeProduit(),
      id: this.editForm.get(['id'])!.value,
      prixAvance: this.editForm.get(['prixAvance'])!.value,
      prixRestant: this.editForm.get(['prixRestant'])!.value,
      status: this.editForm.get(['status'])!.value,
      dateCommandeProduit: this.editForm.get(['dateCommandeProduit'])!.value,
      dateLastModified: this.editForm.get(['dateLastModified'])!.value,
      produits: this.editForm.get(['produits'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
