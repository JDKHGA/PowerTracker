-- 1. Profiles Table (Linked to Supabase Auth)
create table profiles (
  id uuid references auth.users on delete cascade not null primary key,
  email text,
  full_name text,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 2. Meters Table
create table meters (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references profiles(id) on delete cascade not null,
  name text not null,
  meter_number text unique not null,
  type text not null, -- 'Prepaid' or 'Postpaid'
  balance_kwh double precision default 0.0 not null,
  balance_ghs double precision default 0.0 not null,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 3. Tokens Table
create table tokens (
  id uuid default gen_random_uuid() primary key,
  meter_id uuid references meters(id) on delete cascade not null,
  token_code text not null,
  amount numeric not null,
  units numeric not null,
  purchase_date date not null,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 4. Usage Logs Table
create table usage_logs (
  id uuid default gen_random_uuid() primary key,
  meter_id uuid references meters(id) on delete cascade not null,
  usage_kwh numeric not null,
  logged_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- SET UP ROW LEVEL SECURITY (RLS)
-- This ensures users can only access their own data

alter table profiles enable row level security;
alter table meters enable row level security;
alter table tokens enable row level security;
alter table usage_logs enable row level security;

-- Policies for Profiles
create policy "Users can view own profile" on profiles for select using (auth.uid() = id);
create policy "Users can update own profile" on profiles for update using (auth.uid() = id);

-- Policies for Meters
create policy "Users can manage their own meters" on meters
  for all using (auth.uid() = user_id);

-- Policies for Tokens (Access via Meter ownership)
create policy "Users can manage tokens for their meters" on tokens
  for all using (
    exists (
      select 1 from meters
      where meters.id = tokens.meter_id
      and meters.user_id = auth.uid()
    )
  );

-- Policies for Usage Logs (Access via Meter ownership)
create policy "Users can manage usage logs for their meters" on usage_logs
  for all using (
    exists (
      select 1 from meters
      where meters.id = usage_logs.meter_id
      and meters.user_id = auth.uid()
    )
  );


-- Trigger: Automatically create a profile entry when a user signs up
create function public.handle_new_user()
returns trigger as $$
begin
  insert into public.profiles (id, email, full_name)
  values (new.id, new.email, new.raw_user_meta_data->>'full_name');
  return new;
end;
$$ language plpgsql security definer;

create trigger on_auth_user_created
  after insert on auth.users
  for each row execute procedure public.handle_new_user();

-- 5. User Settings Table
create table user_settings (
  user_id uuid references profiles(id) on delete cascade not null primary key,
  notifications_enabled boolean default false not null,
  alert_threshold double precision default 10.0 not null,
  backup_enabled boolean default false not null,
  updated_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 6. Device Tokens Table (for Push Notifications)
create table device_tokens (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references profiles(id) on delete cascade not null,
  fcm_token text not null,
  platform text not null,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null,
  unique(user_id, fcm_token)
);

-- RLS for new tables
alter table user_settings enable row level security;
alter table device_tokens enable row level security;

create policy "Users can manage own settings" on user_settings
  for all using (auth.uid() = user_id);

create policy "Users can manage own device tokens" on device_tokens
  for all using (auth.uid() = user_id);

-- Trigger: Automatically create default settings for new user
create function public.handle_new_user_settings()
returns trigger as $$
begin
  insert into public.user_settings (user_id)
  values (new.id);
  return new;
end;
$$ language plpgsql security definer;

create trigger on_auth_user_created_settings
  after insert on auth.users
  for each row execute procedure public.handle_new_user_settings();
