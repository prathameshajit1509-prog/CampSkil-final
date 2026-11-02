(function(){
 const API='/api';
 async function request(path, options={}) {
   const response=await fetch(API+path,{headers:{'Content-Type':'application/json',...(options.headers||{})},...options});
   if(!response.ok) throw new Error('API request failed ('+response.status+')');
   return response.status===204?null:response.json();
 }
 function panel(title,id){const section=document.createElement('section');section.id=id;section.style.cssText='margin:24px;padding:20px;border:1px solid var(--cs-border,#d1d5db);border-radius:16px;background:var(--cs-card,#fff);color:var(--cs-text,#1f2937)';section.innerHTML='<h2 style="font-size:1.2rem;font-weight:700;margin-bottom:12px">'+title+'</h2><p>Loading from CampusConnect backend…</p>';return section;}
 function esc(s){return String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));}
 async function status(){try{await request('/health');document.documentElement.dataset.backend='connected';}catch(e){document.documentElement.dataset.backend='offline';}}
 async function loadDrives(){
  if(!location.pathname.endsWith('campus-drives.html')&&!location.pathname.endsWith('dashboard.html'))return;
  const section=panel('Live on-campus drives','apiDrivePanel');const main=document.querySelector('main')||document.body;main.appendChild(section);
  try{const data=await request('/drives');section.innerHTML='<h2 style="font-size:1.2rem;font-weight:700;margin-bottom:12px">Live on-campus drives <small style="font-size:.75rem;font-weight:400">(from backend)</small></h2>';
   if(!data.length){section.insertAdjacentHTML('beforeend','<p>No drives yet. Add one through POST /api/admin/drives.</p>');return;}
   data.forEach(d=>{const card=document.createElement('article');card.style.cssText='padding:14px;margin:10px 0;border:1px solid var(--cs-border,#ddd);border-radius:12px';card.innerHTML='<h3 style="font-weight:700">'+esc(d.companyName)+'</h3><p>'+esc(d.jobProfile)+'</p><p style="opacity:.8">'+esc(d.eligibilityCriteria)+'</p><p style="font-size:.9rem">Interview: '+esc(d.interviewDate||'To be announced')+'</p><a style="display:inline-block;margin-top:8px;color:#2698ad;font-weight:600" href="/api/drives/'+d.id+'/pdf">Download drive PDF</a>';section.appendChild(card);});
  }catch(e){section.innerHTML='<h2 style="font-weight:700;margin-bottom:8px">Backend drives</h2><p>Could not load drives. Start Spring Boot and check the database settings.</p>';}
 }
 async function loadRoles(){
  if(!location.pathname.endsWith('prep-guide.html'))return;
  const section=panel('Role-based skill mapping','apiSkillPanel');(document.querySelector('main')||document.body).appendChild(section);
  try{const data=await request('/skills/roles');section.innerHTML='<h2 style="font-size:1.2rem;font-weight:700;margin-bottom:12px">Role-based skill mapping <small style="font-size:.75rem;font-weight:400">(from backend)</small></h2>';
   if(!data.length){section.insertAdjacentHTML('beforeend','<p>No roles available yet.</p>');return;}
   data.forEach(r=>{const card=document.createElement('article');card.style.cssText='padding:12px;margin:8px 0;border:1px solid var(--cs-border,#ddd);border-radius:10px';card.innerHTML='<strong>'+esc(r.roleTitle)+'</strong><p style="margin-top:6px">'+(r.skills||[]).map(s=>'<span style="display:inline-block;margin:3px;padding:4px 8px;border-radius:8px;background:var(--cs-elevated,#f3f4f6)">'+esc(s.skillName)+' · '+esc(s.category)+'</span>').join('')+'</p>';section.appendChild(card);});
  }catch(e){section.innerHTML='<h2 style="font-weight:700">Role-based skill mapping</h2><p>Could not load roles. Start the backend and check the database settings.</p>';}
 }
 document.addEventListener('DOMContentLoaded',()=>{
  status();
  // Injected demo panels disabled to preserve original page layout
  // loadDrives();
  // loadRoles();
 });
 window.CampusConnectAPI={request};
})();