import { useState } from 'react';

interface FormState {
  [key: string]: string;
}

interface UseFormReturn {
  formData: FormState;
  error: string;
  loading: boolean;
  handleChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  setError: (error: string) => void;
  setLoading: (loading: boolean) => void;
  resetForm: () => void;
}

export const useForm = (initialState: FormState): UseFormReturn => {
  const [formData, setFormData] = useState<FormState>(initialState);
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const resetForm = () => {
    setFormData(initialState);
    setError('');
    setLoading(false);
  };

  return {
    formData,
    error,
    loading,
    handleChange,
    setError,
    setLoading,
    resetForm
  };
}; 