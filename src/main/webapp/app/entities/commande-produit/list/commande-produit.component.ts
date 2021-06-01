import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommandeProduit } from '../commande-produit.model';
import { CommandeProduitService } from '../service/commande-produit.service';
import { CommandeProduitDeleteDialogComponent } from '../delete/commande-produit-delete-dialog.component';

@Component({
  selector: 'jhi-commande-produit',
  templateUrl: './commande-produit.component.html',
})
export class CommandeProduitComponent implements OnInit {
  commandeProduits?: ICommandeProduit[];
  isLoading = false;

  constructor(protected commandeProduitService: CommandeProduitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.commandeProduitService.query().subscribe(
      (res: HttpResponse<ICommandeProduit[]>) => {
        this.isLoading = false;
        this.commandeProduits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICommandeProduit): number {
    return item.id!;
  }

  delete(commandeProduit: ICommandeProduit): void {
    const modalRef = this.modalService.open(CommandeProduitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.commandeProduit = commandeProduit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
