import { MembershipList } from "./Type";

export const fetchMembers = async (): Promise<MembershipList> => {
    return fetch('http://localhost:8040/api/course/members')
        .then(res => res.json());
}

export const fetchMembersByNameOrEmail = async (name: string, email: string): Promise<MembershipList> => {
    const url = new URL('http://localhost:8040/api/course/members');
    url.searchParams.set('name', name);
    url.searchParams.set('email', email);
    return fetch(url)
        .then(res => res.json());
}