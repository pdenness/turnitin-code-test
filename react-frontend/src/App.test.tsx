import { act, fireEvent, render, screen } from '@testing-library/react';
import React from 'react';
import { describe, it, jest } from '@jest/globals';
import App from './App';
import { fetchMembers } from './Api';
import { MembershipList } from './Type';

jest.mock('./Api');

describe('App', () => {
  const memberships: MembershipList = {
    memberships: [{
      id: 'A',
      user_id: '00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c',
      role: 'student',
      user: {
        id: '00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c',
        name: 'test Name',
        email: 'test@gmail.com'
      }
    },
    {
      id: 'B',
      user_id: 'f57975d2-e6ae-4f4a-aada-ee6cdcede0d1',
      role: 'instructor',
      user: {
        id: 'f57975d2-e6ae-4f4a-aada-ee6cdcede0d1',
        name: 'test2 Name',
        email: 'Test2@gmail.com'
      }
    }],
  };

  beforeEach(() => {
    (fetchMembers as jest.Mock).mockResolvedValue(memberships);
    render(<App isEdit={false} />);
  });

  it('should display a search box with no results', () => {
    const search = screen.getByRole('textbox');

    expect(search).toBeVisible();
    expect(screen.queryByText('test@gmail.com')).not.toBeInTheDocument();
    expect(screen.queryByText('test Name')).not.toBeInTheDocument();
  });

  it('should display a fetch memberships button with results after click', async () => {
    
    const button = screen.getByRole('button', {
      name: /Fetch Memberships/i,
    });

    await act(async () => fireEvent.click(button));

    expect(screen.getByText('test@gmail.com')).toBeVisible();
    expect(screen.getByText('test Name')).toBeVisible();    
    
    expect(screen.getByText('Test2@gmail.com')).toBeVisible();
    expect(screen.getByText('test2 Name')).toBeVisible();
  });

  describe('Search', () => {
    beforeEach(async () => {
      const button = screen.getByRole('button', {
        name: /Fetch Memberships/i,
      });
      await act(async () => fireEvent.click(button));
    });

    it('should filter name on search box', async () => {
      const search = screen.getByRole('textbox');
  
      await act(async () => fireEvent.input(search, { target: { value: 'test Name' } }));

      expect(screen.getByText('test@gmail.com')).toBeVisible();
      expect(screen.getByText('test Name')).toBeVisible();   
      
      expect(screen.queryByText('Test2@gmail.com')).not.toBeInTheDocument();
      expect(screen.queryByText('test2 Name')).not.toBeInTheDocument();
    });

    it('should filter name on search box with mismatching case', async () => {
      const search = screen.getByRole('textbox');
  
      await act(async () => fireEvent.input(search, { target: { value: 'test Name' } }));

      expect(screen.getByText('test@gmail.com')).toBeVisible();
      expect(screen.getByText('test Name')).toBeVisible();   
      
      expect(screen.queryByText('Test2@gmail.com')).not.toBeInTheDocument();
      expect(screen.queryByText('test2 Name')).not.toBeInTheDocument();
    });

    it('should filter email on search box', async () => {
      const search = screen.getByRole('textbox');
  
      await act(async () => fireEvent.input(search, { target: { value: 'Test2@gmail.com' } }));

      expect(screen.getByText('Test2@gmail.com')).toBeVisible();
      expect(screen.getByText('test2 Name')).toBeVisible();   
      
      expect(screen.queryByText('test@gmail.com')).not.toBeInTheDocument();
      expect(screen.queryByText('test Name')).not.toBeInTheDocument();
    });

    it('should filter email on search box with mismatching case', async () => {
      const search = screen.getByRole('textbox');
  
      await act(async () => fireEvent.input(search, { target: { value: 'test2@gmail.com' } }));

      expect(screen.getByText('Test2@gmail.com')).toBeVisible();
      expect(screen.getByText('test2 Name')).toBeVisible();   
      
      expect(screen.queryByText('test@gmail.com')).not.toBeInTheDocument();
      expect(screen.queryByText('test Name')).not.toBeInTheDocument();
    });
  });

  describe('modal', () => {
    beforeEach(async () => {
      const button = screen.getByRole('button', {
        name: /Fetch Memberships/i,
      });
      await act(async () => fireEvent.click(button));
    });

    it('should show modal when details button is clicked', async () => {
      const button = screen.getAllByRole('button', {
        name: /Details/i,
      })[0];

      expect(screen.queryByText('Membership ID: A')).not.toBeInTheDocument();
      expect(screen.queryByText('User ID: 00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c')).not.toBeInTheDocument();
      expect(screen.queryByText('Role: student')).not.toBeInTheDocument();
      expect(screen.queryByText('Email: test@gmail.com')).not.toBeInTheDocument();
      expect(screen.queryByText('Name: test Name')).not.toBeInTheDocument();

      await act(async () => fireEvent.click(button));

      expect(screen.getByText('Membership ID: A')).toBeVisible();
      expect(screen.getByText('User ID: 00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c')).toBeVisible();
      expect(screen.getByText('Role: student')).toBeVisible();   
      expect(screen.getByText('Email: test@gmail.com')).toBeVisible();   
      expect(screen.getByText('Name: test Name')).toBeVisible();   
    });

    it('should close modal when x button is clicked', async () => {
      const button = screen.getAllByRole('button', {
        name: /Details/i,
      })[0];
      await act(async () => fireEvent.click(button));

      const closeBtn = screen.getByRole('button', {
        name: /Close/i,
      });

      await act(async () => fireEvent.click(closeBtn));

      expect(screen.queryByText('Membership ID: A')).not.toBeInTheDocument();
      expect(screen.queryByText('User ID: 00ad4fc8-e56e-4098-aaf3-1aff93a7bc4c')).not.toBeInTheDocument();
      expect(screen.queryByText('Role: student')).not.toBeInTheDocument();
      expect(screen.queryByText('Email: test@gmail.com')).not.toBeInTheDocument();
      expect(screen.queryByText('Name: test Name')).not.toBeInTheDocument();
    });
  });

});
