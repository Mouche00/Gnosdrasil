export interface ConnectedStep {
  id: string;
  label: string;
  connectedSteps: ConnectedStep[];
}

export interface Step {
  id: string;
  label: string;
  connectedSteps: ConnectedStep[];
}

export interface Roadmap {
  id: string | null;
  title: string;
  steps: Step[];
} 