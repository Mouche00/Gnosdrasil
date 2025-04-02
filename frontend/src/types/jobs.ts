export interface JobTitle {
  title: string;
  count: number;
}

export interface Skill {
  skill: string;
  count: number;
}

export interface DailyJobCount {
  date: string;
  count: number;
}

export interface Company {
  company: string;
  count: number;
}

export interface ExperienceLevelDistribution {
  [key: string]: number;
}

export interface JobTypeDistribution {
  [key: string]: number;
}

export interface Job {
  jobId: string;
  title: string;
  company: string;
  location: string;
  salary: string | null;
  description: string;
  datePosted: string;
  url: string;
  applyUrl: string;
  jobType: string;
  experienceLevel: string;
}

export interface JobResponse {
  topJobTitles: JobTitle[];
  topSkills: Skill[];
  dailyJobCounts: DailyJobCount[];
  topCompanies: Company[];
  experienceLevelDistribution: ExperienceLevelDistribution;
  jobTypeDistribution: JobTypeDistribution;
  jobs: Job[];
}

export interface RoadmapStep {
  id: string;
  label: string;
  connectedSteps: RoadmapStep[];
}

export interface Roadmap {
  id: string | null;
  title: string;
  steps: RoadmapStep[];
} 