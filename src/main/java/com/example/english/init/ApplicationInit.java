package com.example.english.init;

import com.example.english.data.entity.*;
import com.example.english.data.entity.enumerations.RoleEnum;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.ContentRepository;
import com.example.english.data.repository.GrammarCategoryRepository;
import com.example.english.service.GrammarCategoryService;
import com.example.english.service.RoleService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.english.data.entity.enumerations.LevelOfLanguage.randomLevel;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationInit implements CommandLineRunner {
    private final RoleService roleService;
    private final UserService userService;
    private final ModelMapper mapper;
    private final GrammarCategoryService grammarCategoryService;
    private final GrammarCategoryRepository repository;
    private final ContentRepository contentRepository;

    @Override
    public void run(String... args) {
        if (roleService.getRoleCount() == 0) {
            roleService.seedRoles(
                    Arrays.stream(RoleEnum.values())
                            .map(r -> new Role(r.name())).collect(Collectors.toList())
            );
        }

        if (userService.getCount() == 0) {


            users().forEach(userService::register);

            User fizz = mapper.map(userService.getUserByName("fizz"), User.class);

            //when running only the integration tests this will fail,
            //if the application is not running
            fizz.getUserProfile().getCategoriesWithWords().addAll(categoryWordsSet());

            User zxc = mapper.map(userService.getUserByName("zxc"), User.class);
            User wow = mapper.map(userService.getUserByName("wow"), User.class);
            Role role_moderator = roleService.getRoleByName("ROLE_MODERATOR");
            Role role_admin = roleService.getRoleByName("ROLE_ADMIN");

            wow.getAuthorities().addAll(
                    new ArrayList<>() {{
                        add(role_moderator);
                        add(role_admin);
                    }}
            );

            zxc.getAuthorities().add(role_moderator);
            userService.updateUser(zxc);
            userService.updateUser(wow);
            userService.updateUser(fizz);
        }

        if (contentRepository.count() == 0) {
            Content[] contents = contents();

            Arrays.stream(contents).forEach(contentRepository::save);
        }


        if (grammarCategoryService.getCount() == 0) {
            grammarCategoryService.seedCategories(grammarCategories());

            GrammarCategory category = repository.findByName("Present tense").get();

            Content byTitle = contentRepository.findByTitle("What is Lorem Ipsum?");
            Content byTitle1 = contentRepository.findByTitle("Why do we use it?");
            Content byTitle2 = contentRepository.findByTitle("Where can I get some?");

            byTitle.setCategory(category);
            byTitle1.setCategory(category);
            byTitle2.setCategory(category);

            category.getContent().addAll(Arrays.asList(byTitle, byTitle1, byTitle2));

            GrammarCategory category1 = repository.findByName("Nouns").get();
            Content byTitle3 = contentRepository.findByTitle("Neque porro quisquam est quid");
            byTitle3.setCategory(category1);
            category1.getContent().add(byTitle3);

            GrammarCategory category2 = repository.findByName("Conditionals").get();
            Content byTitle4 = contentRepository.findByTitle("Section 1.10.32 of \"de Finibus ");
            byTitle4.setCategory(category2);
            category2.getContent().add(byTitle4);

            repository.saveAll(Arrays.asList(category, category1, category2));
        }
    }

    private static List<UserServiceModel> users() {
        return Arrays.asList(
                //ADMIN && ROOT_ADMIN
                new UserServiceModel("fizz", "fizz", "fizz@fizz.fizz"),
                new UserServiceModel("fizz2", "fizz", "fizz22@fizz.fizz"),
                new UserServiceModel("fizz22", "fizz", "fizz222@fizz.fizz"),

                //MODERATOR && ADMIN
                new UserServiceModel("wow", "wow", "wow@wow.wow"),

                //MODERATOR
                new UserServiceModel("zxc", "zxc", "zxc@zxc.zxc"),

                //USER
                new UserServiceModel("Bai ИВАН", "Ваньо666", "BAT_IVAN@ВАНьО.БГ")
        );
    }

    private static GrammarCategory[] grammarCategories() {
        return new GrammarCategory[]{
                new GrammarCategory("Present tense"),
                new GrammarCategory("Present perfect tense"),
                new GrammarCategory("Past tense"),
                new GrammarCategory("Future tense"),
                new GrammarCategory("Adjectives and adverbs"),
                new GrammarCategory("Conditionals"),
                new GrammarCategory("Passive and active voice"),
                new GrammarCategory("Reported Speech"),
                new GrammarCategory("Relative clauses"),
                new GrammarCategory("Nouns"),
        };
    }

    private Content[] contents() {
        return new Content[]{
                new Content("What is Lorem Ipsum?",
                        randomLevel(),
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                        mapper.map(userService.getUserByName("fizz"), User.class)
                ),
                new Content("Where can I get some?",
                        randomLevel(),
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Viverra justo nec ultrices dui sapien. Vitae nunc sed velit dignissim sodales. Nec feugiat nisl pretium fusce id velit ut tortor. Lobortis mattis aliquam faucibus purus in massa tempor nec. Scelerisque varius morbi enim nunc faucibus a pellentesque sit. Donec adipiscing tristique risus nec feugiat in. Aliquet eget sit amet tellus cras. Nunc congue nisi vitae suscipit tellus mauris a diam. Pharetra convallis posuere morbi leo urna molestie. Integer feugiat scelerisque varius morbi. Lorem sed risus ultricies tristique nulla aliquet enim tortor at. Id semper risus in hendrerit gravida. Velit ut tortor pretium viverra suspendisse. Adipiscing commodo elit at imperdiet dui accumsan.\n" +
                                "\n" +
                                "Donec enim diam vulputate ut pharetra. Amet mattis vulputate enim nulla. Nam aliquam sem et tortor consequat id porta nibh. Posuere urna nec tincidunt praesent semper feugiat nibh sed pulvinar. Donec et odio pellentesque diam. Mattis molestie a iaculis at erat pellentesque adipiscing commodo elit. Malesuada pellentesque elit eget gravida cum sociis natoque penatibus et. Integer feugiat scelerisque varius morbi enim nunc faucibus a pellentesque. Id diam maecenas ultricies mi eget. Volutpat ac tincidunt vitae semper. Nunc sed blandit libero volutpat. Amet porttitor eget dolor morbi non arcu. Nunc mattis enim ut tellus elementum sagittis vitae. Diam donec adipiscing tristique risus nec feugiat. Blandit turpis cursus in hac habitasse. Nunc sed id semper risus in hendrerit gravida. Sodales ut eu sem integer vitae justo eget magna. Dictum varius duis at consectetur lorem. Euismod lacinia at quis risus sed vulputate odio ut enim.\n" +
                                "\n" +
                                "Eget egestas purus viverra accumsan in nisl nisi. Proin sed libero enim sed faucibus turpis in eu mi. Euismod elementum nisi quis eleifend quam adipiscing vitae proin. Bibendum at varius vel pharetra. Sit amet dictum sit amet. Pellentesque adipiscing commodo elit at imperdiet dui accumsan sit amet. In tellus integer feugiat scelerisque varius morbi enim nunc. Risus feugiat in ante metus. Amet commodo nulla facilisi nullam vehicula. Nisi est sit amet facilisis magna etiam tempor orci. Elementum nisi quis eleifend quam adipiscing vitae proin sagittis nisl. Amet consectetur adipiscing elit pellentesque. Platea dictumst quisque sagittis purus sit amet volutpat consequat mauris. Turpis in eu mi bibendum neque egestas. Cras sed felis eget velit. Et tortor at risus viverra adipiscing.\n" +
                                "\n" +
                                "Iaculis urna id volutpat lacus laoreet non curabitur gravida. Suscipit tellus mauris a diam maecenas. Varius sit amet mattis vulputate enim nulla aliquet porttitor lacus. Sed libero enim sed faucibus turpis in eu. Volutpat commodo sed egestas egestas fringilla phasellus faucibus scelerisque eleifend. Non enim praesent elementum facilisis leo vel fringilla est ullamcorper. Vestibulum lorem sed risus ultricies tristique nulla aliquet. Nunc lobortis mattis aliquam faucibus purus in massa. Turpis massa tincidunt dui ut ornare lectus sit amet. Orci phasellus egestas tellus rutrum. Eget felis eget nunc lobortis mattis aliquam faucibus purus. Quis ipsum suspendisse ultrices gravida. Aliquam sem fringilla ut morbi tincidunt. Id velit ut tortor pretium viverra suspendisse potenti nullam.\n" +
                                "\n" +
                                "Libero justo laoreet sit amet cursus sit amet. Elit duis tristique sollicitudin nibh sit. Egestas sed sed risus pretium. Risus quis varius quam quisque id. Purus ut faucibus pulvinar elementum integer. Amet risus nullam eget felis eget nunc lobortis mattis. Metus vulputate eu scelerisque felis. Aliquet eget sit amet tellus cras adipiscing. Adipiscing diam donec adipiscing tristique risus nec. Nisl vel pretium lectus quam. Malesuada fames ac turpis egestas. Diam donec adipiscing tristique risus nec. Dignissim enim sit amet venenatis urna. Et tortor at risus viverra adipiscing at. Diam in arcu cursus euismod quis viverra. In aliquam sem fringilla ut morbi tincidunt augue interdum velit.",
                        mapper.map(userService.getUserByName("fizz"), User.class)
                ),
                new Content("Section 1.10.32 of \"de Finibus ",
                        randomLevel(),
                        "orem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Proin sagittis nisl rhoncus mattis rhoncus. Quam quisque id diam vel quam elementum pulvinar etiam non. Mauris sit amet massa vitae tortor condimentum lacinia. Tristique et egestas quis ipsum suspendisse ultrices gravida dictum fusce. Sed faucibus turpis in eu mi bibendum neque egestas. Vel quam elementum pulvinar etiam non. Sit amet purus gravida quis blandit turpis. Est ante in nibh mauris cursus mattis molestie. Habitant morbi tristique senectus et netus. Nisi scelerisque eu ultrices vitae auctor eu. In aliquam sem fringilla ut. Ante in nibh mauris cursus mattis molestie a iaculis at.\n" +
                                "\n" +
                                "Ut ornare lectus sit amet est placerat in egestas. Vestibulum lorem sed risus ultricies. Orci phasellus egestas tellus rutrum. Ut ornare lectus sit amet est placerat in egestas erat. Ac orci phasellus egestas tellus rutrum tellus pellentesque eu tincidunt. Facilisi morbi tempus iaculis urna id volutpat lacus. Sit amet justo donec enim diam vulputate ut. Eget lorem dolor sed viverra ipsum nunc aliquet. At lectus urna duis convallis convallis tellus. Neque convallis a cras semper. Risus at ultrices mi tempus imperdiet. Phasellus vestibulum lorem sed risus ultricies tristique nulla aliquet enim. Cras pulvinar mattis nunc sed.\n" +
                                "\n" +
                                "Lacinia at quis risus sed vulputate odio ut enim. Aliquam sem et tortor consequat. Nullam vehicula ipsum a arcu cursus vitae congue mauris. Malesuada fames ac turpis egestas maecenas. Scelerisque felis imperdiet proin fermentum leo vel. Sit amet tellus cras adipiscing enim eu turpis egestas. Sapien pellentesque habitant morbi tristique senectus. Et egestas quis ipsum suspendisse ultrices. Eu augue ut lectus arcu bibendum at varius. Habitant morbi tristique senectus et netus et malesuada fames. Lobortis feugiat vivamus at augue eget arcu dictum. Sit amet justo donec enim diam. Volutpat consequat mauris nunc congue nisi vitae suscipit tellus. Aliquam nulla facilisi cras fermentum odio eu feugiat pretium. Pretium aenean pharetra magna ac placerat vestibulum. Eget lorem dolor sed viverra. Ultrices gravida dictum fusce ut placerat orci. Fermentum iaculis eu non diam phasellus.\n" +
                                "\n" +
                                "Vitae ultricies leo integer malesuada nunc vel risus commodo viverra. Integer quis auctor elit sed. Fames ac turpis egestas maecenas. Iaculis at erat pellentesque adipiscing commodo elit at imperdiet. Tincidunt vitae semper quis lectus nulla at volutpat diam. Porttitor lacus luctus accumsan tortor posuere. Non sodales neque sodales ut etiam. Orci dapibus ultrices in iaculis nunc. Amet mauris commodo quis imperdiet massa tincidunt nunc pulvinar sapien. Duis ut diam quam nulla porttitor massa. Lacinia quis vel eros donec ac. Euismod in pellentesque massa placerat duis ultricies lacus sed turpis. Neque sodales ut etiam sit amet. Augue neque gravida in fermentum et sollicitudin ac orci phasellus. Tortor vitae purus faucibus ornare.",
                        mapper.map(userService.getUserByName("Bai ИВАН"), User.class)
                ),
                new Content("Why do we use it?",
                        randomLevel(),
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Faucibus purus in massa tempor nec. Pretium vulputate sapien nec sagittis. Lectus vestibulum mattis ullamcorper velit sed ullamcorper. Cursus metus aliquam eleifend mi in nulla posuere sollicitudin. Massa enim nec dui nunc mattis. Nisi quis eleifend quam adipiscing vitae. Enim ut tellus elementum sagittis vitae et leo duis. In dictum non consectetur a erat nam. Lacus vestibulum sed arcu non. Justo nec ultrices dui sapien eget mi. Tincidunt nunc pulvinar sapien et.\n" +
                                "\n" +
                                "Ut pharetra sit amet aliquam id diam. Quam nulla porttitor massa id neque aliquam vestibulum morbi. Et ligula ullamcorper malesuada proin. Est ultricies integer quis auctor. Sit amet purus gravida quis blandit turpis. Donec adipiscing tristique risus nec feugiat in fermentum posuere urna. Enim diam vulputate ut pharetra sit. Aliquam etiam erat velit scelerisque in dictum. Aliquet porttitor lacus luctus accumsan tortor posuere ac ut. Nibh tellus molestie nunc non blandit. Scelerisque varius morbi enim nunc faucibus a pellentesque. Enim ut sem viverra aliquet eget sit. Sit amet luctus venenatis lectus magna fringilla. Posuere ac ut consequat semper viverra nam libero justo laoreet. Ultricies leo integer malesuada nunc vel risus. Amet mattis vulputate enim nulla. Ac tincidunt vitae semper quis lectus nulla at volutpat. Varius quam quisque id diam vel quam elementum pulvinar. Eu mi bibendum neque egestas. Purus gravida quis blandit turpis cursus.\n" +
                                "\n" +
                                "Nisl purus in mollis nunc sed id semper. Tincidunt lobortis feugiat vivamus at. Eu augue ut lectus arcu bibendum. Id eu nisl nunc mi ipsum faucibus vitae aliquet nec. Turpis egestas maecenas pharetra convallis. Vitae ultricies leo integer malesuada nunc. Odio pellentesque diam volutpat commodo sed egestas egestas. Porttitor massa id neque aliquam vestibulum morbi blandit. Sed odio morbi quis commodo. Velit euismod in pellentesque massa placerat duis ultricies lacus sed. Adipiscing at in tellus integer. In mollis nunc sed id semper risus in hendrerit. Proin fermentum leo vel orci porta non pulvinar neque.",
                        mapper.map(userService.getUserByName("wow"), User.class)
                ),
                new Content("Neque porro quisquam est quid",
                        randomLevel(),
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ultricies mi quis hendrerit dolor. Consequat mauris nunc congue nisi vitae suscipit tellus mauris. Lorem ipsum dolor sit amet consectetur adipiscing. Posuere ac ut consequat semper viverra nam libero justo laoreet. Gravida cum sociis natoque penatibus et magnis dis. Elit pellentesque habitant morbi tristique senectus. Leo vel orci porta non pulvinar neque laoreet suspendisse. Sit amet consectetur adipiscing elit duis. Adipiscing diam donec adipiscing tristique risus nec. Auctor augue mauris augue neque gravida in fermentum et. Mi bibendum neque egestas congue quisque egestas. Mi bibendum neque egestas congue quisque egestas diam. Nunc eget lorem dolor sed viverra ipsum nunc aliquet bibendum. Tortor pretium viverra suspendisse potenti nullam ac. Imperdiet proin fermentum leo vel orci porta non pulvinar. Quam nulla porttitor massa id. Enim ut sem viverra aliquet eget sit amet. Eu mi bibendum neque egestas congue quisque egestas diam.\n" +
                                "\n" +
                                "Quis varius quam quisque id diam. Volutpat odio facilisis mauris sit amet massa vitae tortor condimentum. Eget mauris pharetra et ultrices neque ornare aenean. Tellus in hac habitasse platea dictumst vestibulum rhoncus. Feugiat nibh sed pulvinar proin gravida hendrerit lectus a. Morbi tristique senectus et netus et malesuada fames ac. Sed cras ornare arcu dui vivamus arcu felis. Malesuada nunc vel risus commodo viverra maecenas accumsan. Risus nullam eget felis eget nunc. Leo vel fringilla est ullamcorper eget nulla. Aliquet lectus proin nibh nisl condimentum id venenatis a. Neque egestas congue quisque egestas diam. Libero enim sed faucibus turpis in eu mi. Pellentesque elit eget gravida cum. Risus ultricies tristique nulla aliquet. Ut tortor pretium viverra suspendisse potenti nullam. Malesuada nunc vel risus commodo viverra.\n" +
                                "\n" +
                                "Sociis natoque penatibus et magnis. Vulputate ut pharetra sit amet. Urna et pharetra pharetra massa massa ultricies mi quis. Nunc vel risus commodo viverra. Mattis rhoncus urna neque viverra justo nec ultrices. Nunc sed blandit libero volutpat sed cras. Ultricies leo integer malesuada nunc vel risus commodo. Id ornare arcu odio ut sem nulla pharetra diam. Mauris pharetra et ultrices neque. Adipiscing elit pellentesque habitant morbi tristique senectus. Aliquam faucibus purus in massa tempor. Sed viverra tellus in hac. Proin sed libero enim sed. Adipiscing elit ut aliquam purus sit. Viverra justo nec ultrices dui sapien. At varius vel pharetra vel. In iaculis nunc sed augue lacus viverra.\n" +
                                "\n" +
                                "Urna condimentum mattis pellentesque id nibh tortor id. Pellentesque habitant morbi tristique senectus et netus et malesuada. Augue eget arcu dictum varius duis. A scelerisque purus semper eget duis at tellus at. Neque volutpat ac tincidunt vitae semper. Bibendum est ultricies integer quis auctor elit sed vulputate mi. Phasellus egestas tellus rutrum tellus pellentesque. Sollicitudin nibh sit amet commodo nulla facilisi nullam. Arcu dui vivamus arcu felis bibendum ut tristique et egestas. In iaculis nunc sed augue lacus viverra. Vel risus commodo viverra maecenas accumsan lacus vel. Pharetra pharetra massa massa ultricies mi quis. Purus sit amet luctus venenatis lectus magna fringilla.",
                        mapper.map(userService.getUserByName("zxc"), User.class)
                ),
        };
    }

    private Set<CategoryWords> categoryWordsSet() {
        return new HashSet<>() {{
            add(new CategoryWords("Unit 10 from C1 Advanced Face2Face book",
                    new ArrayList<>() {{
                        addAll(
                                Arrays.asList(new Word("intrusive", "affecting someone in a way that annoys them and makes them feel uncomfortable,\n" +
                                                "\tbeing involved in a situation where you are not wanted or do not belong"),
                                        new Word("maritime", "connected with the sea in relation to navigation, shipping, etc"),
                                        new Word("ubiquitous", "existing or being everywhere, especially at the same time; omnipresent"),
                                        new Word("disperse", "to spread across or move away over a large area, or to make something do this"),
                                        new Word("hassle", "a situation causing difficulty or trouble"),
                                        new Word("fledgling", "new and without experience"))
                        );
                    }}
            ));
            add(new CategoryWords("IT terminology",
                    new ArrayList<>() {{
                        addAll(
                                Arrays.asList(
                                        new Word("Hoisting", "mechanism where variables and function declarations are moved to the top of their scope before code execution."),
                                        new Word("Multiplexing", "A way of sending multiple signals or streams of information over a communications link at the same time in the form of a single, complex signal"),
                                        new Word("Socket", "An endpoint of a two-way communication link between two programs running on the network."),
                                        new Word("Server", "A piece of computer hardware or software that provides functionality for other programs or devices, called \"clients\"."),
                                        new Word("Proxy", "Server application or appliance that acts as an intermediary for requests from clients seeking resources from servers that provide those resources.")
                                )
                        );
                    }}
            ));
        }};
    }
}
