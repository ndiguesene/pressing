import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommandeProduitComponent } from '../list/commande-produit.component';
import { CommandeProduitDetailComponent } from '../detail/commande-produit-detail.component';
import { CommandeProduitUpdateComponent } from '../update/commande-produit-update.component';
import { CommandeProduitRoutingResolveService } from './commande-produit-routing-resolve.service';

const commandeProduitRoute: Routes = [
  {
    path: '',
    component: CommandeProduitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommandeProduitDetailComponent,
    resolve: {
      commandeProduit: CommandeProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommandeProduitUpdateComponent,
    resolve: {
      commandeProduit: CommandeProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommandeProduitUpdateComponent,
    resolve: {
      commandeProduit: CommandeProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(commandeProduitRoute)],
  exports: [RouterModule],
})
export class CommandeProduitRoutingModule {}
