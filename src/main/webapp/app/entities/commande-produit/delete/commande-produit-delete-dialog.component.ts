import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommandeProduit } from '../commande-produit.model';
import { CommandeProduitService } from '../service/commande-produit.service';

@Component({
  templateUrl: './commande-produit-delete-dialog.component.html',
})
export class CommandeProduitDeleteDialogComponent {
  commandeProduit?: ICommandeProduit;

  constructor(protected commandeProduitService: CommandeProduitService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.commandeProduitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
