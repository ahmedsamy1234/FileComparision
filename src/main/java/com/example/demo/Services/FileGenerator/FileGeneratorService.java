package com.example.demo.Services.FileGenerator;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;




@Service
public class FileGeneratorService {

    @Value("${files.directory}")
    private String filesDirectory;

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int WORD_MIN_LEN = 3;
    private static final int WORD_MAX_LEN = 10;

    private static final List<String> COMMON_WORDS = List.of(
            "the","be","to","of","and","a","in","that","have","I",
            "it","for","not","on","with","he","as","you","do","at",
            "this","but","his","by","from","they","we","say","her","she",
            "or","an","will","my","one","all","would","there","their","what",
            "so","up","out","if","about","who","get","which","go","me",
            "when","make","can","like","time","no","just","him","know","take",
            "people","into","year","your","good","some","could","them","see","other",
            "than","then","now","look","only","come","its","over","think","also",
            "back","after","use","two","how","our","work","first","well","way",
            "even","new","want","because","any","these","give","day","most","us",
            "is","are","was","were","been","being","am","has","had","did","does",
            "having","made","making","find","found","took","take","taken","getting","gave",
            "put","puts","putting","let","lets","letting","got","gets","getting","see",
            "saw","seen","seeing","goes","going","gone","make","makes","making","come",
            "comes","coming","came","think","thinks","thinking","thought","thoughts","know","knows",
            "knowing","knew","known","tell","tells","telling","told","say","says","saying",
            "said","ask","asks","asking","asked","feel","feels","feeling","felt","try",
            "tries","trying","tried","leave","leaves","leaving","left","call","calls","calling",
            "called","keep","keeps","keeping","kept","help","helps","helping","helped","talk",
            "talks","talking","talked","turn","turns","turning","turned","start","starts","starting",
            "started","show","shows","showing","showed","hear","hears","hearing","heard","play",
            "plays","playing","played","run","runs","running","ran","move","moves","moving",
            "moved","live","lives","living","lived","believe","believes","believing","believed","hold",
            "holds","holding","held","bring","brings","bringing","brought","happen","happens","happening",
            "happened","write","writes","writing","wrote","written","provide","provides","providing","provided",
            "sit","sits","sitting","sat","stand","stands","standing","stood","lose","loses",
            "losing","lost","pay","pays","paying","paid","meet","meets","meeting","met",
            "include","includes","including","included","continue","continues","continuing","continued","set","sets",
            "setting","set","learn","learns","learning","learned","change","changes","changing","changed",
            "lead","leads","leading","led","understand","understands","understanding","understood","watch","watches",
            "watching","watched","follow","follows","following","followed","stop","stops","stopping","stopped",
            "create","creates","creating","created","speak","speaks","speaking","spoke","spoken","read",
            "reads","reading","read","allow","allows","allowing","allowed","add","adds","adding",
            "added","spend","spends","spending","spent","grow","grows","growing","grew","grown",
            "open","opens","opening","opened","walk","walks","walking","walked","win","wins",
            "winning","won","offer","offers","offering","offered","remember","remembers","remembering","remembered",
            "love","loves","loving","loved","consider","considers","considering","considered","appear","appears",
            "appearing","appeared","buy","buys","buying","bought","wait","waits","waiting","waited",
            "serve","serves","serving","served","die","dies","dying","died","send","sends",
            "sending","sent","expect","expects","expecting","expected","build","builds","building","built",
            "stay","stays","staying","stayed","fall","falls","falling","fell","fallen","cut",
            "cuts","cutting","cut","reach","reaches","reaching","reached","kill","kills","killing",
            "killed","remain","remains","remaining","remained","suggest","suggests","suggesting","suggested","raise",
            "raises","raising","raised","pass","passes","passing","passed","sell","sells","selling",
            "sold","require","requires","requiring","required","report","reports","reporting","reported","decide",
            "decides","deciding","decided","pull","pulls","pulling","pulled","return","returns","returning",
            "returned","explain","explains","explaining","explained","hope","hopes","hoping","hoped","develop",
            "develops","developing","developed","carry","carries","carrying","carried","break","breaks","breaking",
            "broke","broken","receive","receives","receiving","received","agree","agrees","agreeing","agreed",
            "support","supports","supporting","supported","hit","hits","hitting","hit","produce","produces",
            "producing","produced","eat","eats","eating","ate","eaten","cover","covers","covering",
            "covered","catch","catches","catching","caught","draw","draws","drawing","drew","drawn",
            "choose","chooses","choosing","chose","chosen","cause","causes","causing","caused","point",
            "points","pointing","pointed","listen","listens","listening","listened","realize","realizes","realizing",
            "realized","place","places","placing","placed","close","closes","closing","closed","involve",
            "involves","involving","involved","increase","increases","increasing","increased","thank","thanks","thanking",
            "thanked","describe","describes","describing","described","save","saves","saving","saved","wonder",
            "wonders","wondering","wondered","drop","drops","dropping","dropped","push","pushes","pushing",
            "pushed","plan","plans","planning","planned","fill","fills","filling","filled","fit",
            "fits","fitting","fitted","protect","protects","protecting","protected","ring","rings","ringing",
            "rang","rung","check","checks","checking","checked","miss","misses","missing","missed",
            "finish","finishes","finishing","finished","smile","smiles","smiling","smiled","charge","charges",
            "charging","charged","print","prints","printing","printed","wake","wakes","waking","woke",
            "woken","catch","catches","catching","caught","join","joins","joining","joined","match",
            "matches","matching","matched","fit","fits","fitting","fitted","paint","paints","painting",
            "painted","enter","enters","entering","entered","wish","wishes","wishing","wished","hunt",
            "hunts","hunting","hunted","date","dates","dating","dated","count","counts","counting",
            "counted","study","studies","studying","studied","train","trains","training","trained","attack",
            "attacks","attacking","attacked","watch","watches","watching","watched","dance","dances","dancing",
            "danced","fly","flies","flying","flew","flown","invite","invites","inviting","invited","marry",
            "marries","marrying","married","play","plays","playing","played","race","races","racing",
            "raced","cook","cooks","cooking","cooked","bake","bakes","baking","baked","jump",
            "jumps","jumping","jumped","sing","sings","singing","sang","sung","swim","swims",
            "swimming","swam","swum","laugh","laughs","laughing","laughed","cry","cries","crying",
            "cried","fight","fights","fighting","fought","rest","rests","resting","rested","shout",
            "shouts","shouting","shouted","win","wins","winning","won","lose","loses","losing",
            "lost","travel","travels","traveling","traveled","study","studies","studying","studied","build",
            "builds","building","built","destroy","destroys","destroying","destroyed","repair","repairs","repairing",
            "repaired","visit","visits","visiting","visited","explore","explores","exploring","explored","improve",
            "improves","improving","improved","reduce","reduces","reducing","reduced","grow","grows","growing",
            "grew","grown","decline","declines","declining","declined","increase","increases","increasing","increased",
            "develop","develops","developing","developed","progress","progresses","progressing","progressed","advance","advances",
            "advancing","advanced","retreat","retreats","retreating","retreated","move","moves","moving","moved",
            "walk","walks","walking","walked","run","runs","running","ran","stop","stops",
            "stopping","stopped","start","starts","starting","started","open","opens","opening","opened",
            "close","closes","closing","closed","lock","locks","locking","locked","unlock","unlocks",
            "unlocking","unlocked","sit","sits","sitting","sat","stand","stands","standing","stood",
            "lie","lies","lying","lay","laid","rise","rises","rising","rose","risen",
            "fall","falls","falling","fell","fallen","jump","jumps","jumping","jumped","climb",
            "climbs","climbing","climbed","crawl","crawls","crawling","crawled","roll","rolls","rolling",
            "rolled","slide","slides","sliding","slid","move","moves","moving","moved","rest",
            "rests","resting","rested","sleep","sleeps","sleeping","slept","dream","dreams","dreaming",
            "dreamed","think","thinks","thinking","thought","know","knows","knowing","knew","known",
            "forget","forgets","forgetting","forgot","forgotten","remember","remembers","remembering","remembered","learn",
            "learns","learning","learned","understand","understands","understanding","understood","believe","believes","believing",
            "believed","doubt","doubts","doubting","doubted","agree","agrees","agreeing","agreed","disagree",
            "disagrees","disagreeing","disagreed","guess","guesses","guessing","guessed","wonder","wonders","wondering",
            "wondered","hope","hopes","hoping","hoped","wish","wishes","wishing","wished","plan",
            "plans","planning","planned","intend","intends","intending","intended","decide","decides","deciding",
            "decided","refuse","refuses","refusing","refused","accept","accepts","accepting","accepted","choose",
            "chooses","choosing","chose","chosen","prefer","prefers","preferring","preferred","like","likes",
            "liking","liked","dislike","dislikes","disliking","disliked","hate","hates","hating","hated",
            "love","loves","loving","loved","enjoy","enjoys","enjoying","enjoyed","feel","feels",
            "feeling","felt","sense","senses","sensing","sensed","see","sees","seeing","saw",
            "seen","look","looks","looking","looked","watch","watches","watching","watched","hear",
            "hears","hearing","heard","listen","listens","listening","listened","sound","sounds","sounding",
            "sounded","smell","smells","smelling","smelled","taste","tastes","tasting","tasted","touch",
            "touches","touching","touched","hold","holds","holding","held","grab","grabs","grabbing",
            "grabbed","pick","picks","picking","picked","drop","drops","dropping","dropped","throw",
            "throws","throwing","threw","thrown","catch","catches","catching","caught","pull","pulls",
            "pulling","pulled","push","pushes","pushing","pushed","carry","carries","carrying","carried",
            "lift","lifts","lifting","lifted","raise","raises","raising","raised","lower","lowers",
            "lowering","lowered","give","gives","giving","gave","given","send","sends","sending",
            "sent","bring","brings","bringing","brought","take","takes","taking","took","taken",
            "get","gets","getting","got","gotten","keep","keeps","keeping","kept","hold",
            "holds","holding","held","put","puts","putting","put","set","sets","setting",
            "set","place","places","placing","placed","remove","removes","removing","removed","replace",
            "replaces","replacing","replaced","change","changes","changing","changed","turn","turns","turning",
            "turned","start","starts","starting","started","begin","begins","beginning","began","begun",
            "end","ends","ending","ended","finish","finishes","finishing","finished","stop","stops",
            "stopping","stopped","pause","pauses","pausing","paused","wait","waits","waiting","waited",
            "continue","continues","continuing","continued","break","breaks","breaking","broke","broken","fix",
            "fixes","fixing","fixed","repair","repairs","repairing","repaired"
    );

    public String generateRandomFile(String fileName, int words) throws IOException {
        Path dirPath = Path.of(filesDirectory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path outputPath = dirPath.resolve(fileName);
        Random rand = new Random();

        try (FileWriter fw = new FileWriter(outputPath.toFile())) {
            for (int i = 0; i < words; i++) {
                String word;
                // 50% chance to use a word from COMMON_WORDS, 50% random word
                if (rand.nextBoolean() && !COMMON_WORDS.isEmpty()) {
                    word = COMMON_WORDS.get(rand.nextInt(COMMON_WORDS.size()));
                } else {
                    word = randomWord(rand);
                }
                fw.write(word + " ");
                if ((i + 1) % 15 == 0) fw.write("\n");
            }
        }

        return outputPath.toAbsolutePath().toString();
    }

    private String randomWord(Random rand) {
        int len = rand.nextInt(WORD_MAX_LEN - WORD_MIN_LEN + 1) + WORD_MIN_LEN;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(ALPHABET.charAt(rand.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}