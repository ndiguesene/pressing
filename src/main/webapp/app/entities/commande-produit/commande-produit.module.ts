import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CommandeProduitComponent } from './list/commande-produit.component';
import { CommandeProduitDetailComponent } from './detail/commande-produit-detail.component';
import { CommandeProduitUpdateComponent } from './update/commande-produit-update.component';
import { CommandeProduitDeleteDialogComponent } from './delete/commande-produit-delete-dialog.component';
import { CommandeProduitRoutingModule } from './route/commande-produit-routing.module';

@NgModule({
  imports: [SharedModule, CommandeProduitRoutingModule],
  declarations: [
    CommandeProduitComponent,
    CommandeProduitDetailComponent,
    CommandeProduitUpdateComponent,
    CommandeProduitDeleteDialogComponent,
  ],
  entryComponents: [CommandeProduitDeleteDialogComponent],
})
export class CommandeProduitModule {}
