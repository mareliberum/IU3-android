#!/usr/bin/env python3
"""Import public department data and image assets from an iu3-ios checkout."""
from pathlib import Path
import argparse,json,re,shutil,textwrap
parser=argparse.ArgumentParser()
parser.add_argument('--ios-checkout', required=True, type=Path)
parser.add_argument('--android-project', type=Path, default=Path(__file__).resolve().parents[1])
args=parser.parse_args()
IOS=args.ios_checkout.resolve();ROOT=args.android_project.resolve()
def put(path,text):
 p=ROOT/path;p.parent.mkdir(parents=True,exist_ok=True);p.write_text(textwrap.dedent(text).lstrip())
for p in (IOS/'IU3/Resources/Data').glob('*.json'):
 q=ROOT/'core/data/src/main/assets/responses'/p.name;q.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(p,q)
# Only parse data initializers; never execute Swift.
TOKEN=re.compile(r'\s+|//[^\n]*|/\*.*?\*/|""".*?"""|"(?:\\.|[^"\\])*"|[A-Za-z_][\w.]*|\.[A-Za-z_]\w*|-?\d+(?:\.\d+)?|.',re.S)
class Parser:
 def __init__(self,s):self.t=[t for t in TOKEN.findall(s) if not t.isspace() and not t.startswith('//') and not t.startswith('/*')];self.i=0
 def pop(self):v=self.t[self.i];self.i+=1;return v
 def peek(self):return self.t[self.i] if self.i<len(self.t) else ''
 def val(self):
  t=self.pop()
  if t.startswith('"""'):
   v=textwrap.dedent(t[3:-3]).strip();return re.sub(r'\\\n\s*','',v)
  if t.startswith('"'):return json.loads(t)
  if t=='nil':return None
  if t in ['true','false']:return t=='true'
  if re.fullmatch(r'-?\d+(\.\d+)?',t):return float(t) if '.' in t else int(t)
  if t in ['[','(']:
   end=']' if t=='[' else ')';items=[];fields={}
   while self.peek()!=end:
    a=self.val()
    if self.peek()==':':self.pop();fields[a['_ref'] if isinstance(a,dict) and '_ref' in a else str(a)]=self.val()
    else:items.append(a)
    if self.peek()==',':self.pop()
    elif self.peek()!=end:raise ValueError(self.t[self.i:self.i+8])
   self.pop();return fields if fields else items
  if self.peek()=='(':
   args=self.val()
   if self.peek()=="!":self.pop()
   if isinstance(args,dict):return dict(args,_type=t.lstrip('.'))
   return {'_type':t.lstrip('.'),'values':args}
  return {'_ref':t.lstrip('.')}
def calls(file,name):
 s=(IOS/file).read_text().replace('practiceCompanies.filter { $0.logo != "CompanyLavochkin" }', 'masterPracticeCompanies');out=[]
 for match in re.finditer(r'\b'+name+r'\(\s*(?:[a-zA-Z]+):',s):
  # Exclude declarations, init calls without literals handled separately.
  p=Parser(s[match.start():]);out.append(p.val())
 return out
def save(name,d):put('core/data/src/main/assets/responses/'+name+'.json',json.dumps(d,ensure_ascii=False,indent=2)+'\n')
h=calls('IU3/Model/Data/HonorData.swift','HonorCard');save('honor',h)
p=calls('IU3/Model/Entities/Home/ProjectModels.swift','Project')[:8]
n=(IOS/'IU3/Model/Entities/Home/NeuroQuestData.swift').read_text()
refs={}
for key in ['title','subtitle','about']:
 refs['NeuroQuest.'+key]=Parser(n.split('static let '+key+' =',1)[1]).val()
refs['NeuroQuest.shots']=calls('IU3/Model/Entities/Home/NeuroQuestData.swift','NeuroShot')
def resolve(x):
 if isinstance(x,dict):
  if '_ref' in x:return refs.get(x['_ref'],x)
  return {k:resolve(v) for k,v in x.items()}
 if isinstance(x,list):return [resolve(a) for a in x]
 return x
save('projects',resolve(p))
s=(IOS/'IU3/Model/Entities/Directions/AdmissionPrograms.swift').read_text()
for k in ['engineeringCareers','practiceCompanies','alumniEmployers']:
 after=re.split(r'static let '+k+r'[^=]*=',s,1)[1];refs[k]=Parser(after).val()
refs['masterPracticeCompanies']=[c for c in refs['practiceCompanies'] if c['logo']!='CompanyLavochkin']
save('programs',resolve(calls('IU3/Model/Entities/Directions/AdmissionPrograms.swift','AdmissionProgram')))
save('achievements',calls('IU3/Model/Entities/Department/DepartmentModels.swift','DepartmentAchievement'))
s=(IOS/'IU3/Model/Entities/Course/TechnologyModels.swift').read_text();save('technologies',Parser(s.split('static let byStack:',1)[1].split('=',1)[1]).val())
# Flatten catalog images into Android asset names without collisions.
assets=ROOT/'core/designsystem/src/main/assets/images';assets.mkdir(parents=True,exist_ok=True)
manifest={}
for p in (IOS/'IU3/Resources/Assets.xcassets').rglob('*.imageset/Contents.json'):
 d=json.loads(p.read_text());files=[i['filename'] for i in d.get('images',[]) if i.get('filename')]
 if not files:continue
 f=p.parent/files[-1]
 if f.suffix.lower() not in ['.png','.jpg','.jpeg','.webp','.gif']:continue
 name=p.parent.stem;out=name+f.suffix.lower();shutil.copy2(f,assets/out);manifest[name]=out
save('image_manifest',manifest)

s=(IOS/'IU3/Model/Data/WebFacultyData.swift').read_text()
save('web_faculty',Parser(s.split('private static let details:',1)[1].split('=',1)[1]).val())
s=(IOS/'IU3/Model/Data/InstructorFaces.swift').read_text()
save('faces',Parser(s.split('static let byAsset:',1)[1].split('=',1)[1]).val())
# Transform only the Swift-derived fixture format; original department JSON remains byte-for-byte intact.
responses=ROOT/'core/data/src/main/assets/responses'
def read(n):return json.loads((responses/(n+'.json')).read_text())
def save(n,v):(responses/(n+'.json')).write_text(json.dumps(v,ensure_ascii=False,indent=2)+'\n')
def publication(row):return {'title':row[0], 'type':row[1]['_ref'], 'year':row[2]}
h=read('honor')
for x in h:
 x['kind']=x['kind']['_ref'];x['works']=[publication(row) for row in x.get('works',[])]
save('honor',h)
projects=read('projects')
for x in projects:
 x['image']=x.pop('imageName',None);x['featured']=x.pop('isFeatured',False);x['tint']=x.get('tint',{'_ref':'blue'})['_ref']
 for link in x.get('links',[]):link['url']=link['url']['string']
 for photo in x.get('gallery',[]):
  values=photo.get('values',[]);photo['image']=photo.get('imageAssetName') or values[0];photo['caption']=photo.get('caption') or values[1]
save('projects',projects)
programs=read('programs')
for x in programs:
 for section in x['sections']:section['type']=section.pop('_type')
save('programs',programs)
tech=read('technologies')
for stack in tech.values():
 for x in stack:x['image']=x.pop('imageAssetName')
save('technologies',tech)
web=read('web_faculty')
for x in web.values():x['publications']=[publication(row) for row in x['publications']]
save('web_faculty',web)

print("Imported department catalogs and images")

s=(IOS/'IU3/Model/Entities/Department/DepartmentModels.swift').read_text().split('struct ContactInfo:',1)[1].split('struct DepartmentLocation:',1)
info={k:json.loads(v) for k,v in re.findall(r'self\.(email|phone|website|address|officeHours)\s*=\s*("(?:\\.|[^"\\])*")',s[0])}
info.update(building='Главный корпус, южное крыло, 5-й этаж',latitude=55.7658,longitude=37.6843)
save('contacts',info)
