import * as dayjs from 'dayjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { IClient } from 'app/entities/client/client.model';
import { StatusCommandeProduit } from 'app/entities/enumerations/status-commande-produit.model';

export interface ICommandeProduit {
  id?: number;
  prixAvance?: number | null;
  prixRestant?: number | null;
  status?: StatusCommandeProduit | null;
  dateCommandeProduit?: dayjs.Dayjs | null;
  dateLastModified?: dayjs.Dayjs | null;
  produit?: IProduit | null;
  client?: IClient | null;
}

export class CommandeProduit implements ICommandeProduit {
  constructor(
    public id?: number,
    public prixAvance?: number | null,
    public prixRestant?: number | null,
    public status?: StatusCommandeProduit | null,
    public dateCommandeProduit?: dayjs.Dayjs | null,
    public dateLastModified?: dayjs.Dayjs | null,
    public produit?: IProduit | null,
    public client?: IClient | null
  ) {}
}

export function getCommandeProduitIdentifier(commandeProduit: ICommandeProduit): number | undefined {
  return commandeProduit.id;
}
