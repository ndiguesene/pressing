import { IUser } from 'app/entities/user/user.model';
import { ICommandeProduit } from 'app/entities/commande-produit/commande-produit.model';

export interface IClient {
  id?: number;
  telephone?: string | null;
  user?: IUser;
  commandeProduits?: ICommandeProduit[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public telephone?: string | null,
    public user?: IUser,
    public commandeProduits?: ICommandeProduit[] | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
