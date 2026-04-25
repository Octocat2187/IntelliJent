import { createClient } from "@supabase/supabase-js";

const supabaseUrl = "https://mtpyanagbimztqiqkrqa.supabase.co";
const supabaseAnonKey = "sb_publishable_P3GVTf8pC_oNSQFHyLNlwg_8boMkWLY";

export const supabase = createClient(supabaseUrl, supabaseAnonKey);