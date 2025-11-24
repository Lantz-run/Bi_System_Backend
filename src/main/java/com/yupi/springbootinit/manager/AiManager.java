package com.yupi.springbootinit.manager;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.request.SparkRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Project: yubi-backend
 * <p>Powered by Lantz On 2025/3/29
 *
 * @author Lantz
 * @version 1.0
 * @Description AiManager
 * @since 1.8
 */
@Service
@Slf4j
public class AiManager {

    @Resource
    private SparkClient sparkClient;

    private final String apiKey = "xxxx";


    /**
     * 向 AI 发送请求
     *
     * @param isNeedTemplate 是否使用模板，进行 AI 生成； true 使用 、false 不使用 ，false 的情况是只想用 AI 不只是生成前端代码
     * @param content        内容
     *                       分析需求：
     *                       分析网站用户的增长情况
     *                       原始数据：
     *                       日期,用户数
     *                       1号,10
     *                       2号,20
     *                       3号,30
     * @return AI 返回的内容
     * '【【【【'
     * <p>
     * '【【【【'
     */
    public String sendMsgToXingHuo(boolean isNeedTemplate, String content) {
        List<SparkMessage> messages = new ArrayList<>();
        if (isNeedTemplate) {
            // AI 生成问题的预设条件
            String predefinedInformation = "请严格按照下面的输出格式生成结果，且不得添加任何多余内容（例如无关文字、注释、代码块标记或反引号）：\n" +
                    "\n" +
                    "'【【【【'" +
                    "{ 生成 Echarts V5 的 option 配置对象 JSON 代码，要求为合法 JSON 格式且不含任何额外内容（如注释或多余字符） } '【【【【' 结论： {\n" +
                    "提供对数据的详细分析结论，内容应尽可能准确、详细，不允许添加其他无关文字或注释 }\n" +
                    "\n" +
                    "示例： 输入： 分析需求： 分析网站用户增长情况，请使用柱状图展示 原始数据： 日期,用户数 1号,10 2号,20 3号,30\n" +
                    "\n" +
                    "期望输出： '【【【【' { \"title\": { \"text\": \"分析网站用户增长情况\" }, \"xAxis\": { \"type\": \"category\", \"data\": [\"1号\", \"2号\", \"3号\"] }, \"yAxis\": { \"type\": \"value\" }, \"series\": [ { \"name\": \"用户数\", \"type\": \"bar\", \"data\": [10, 20, 30] } ] } '【【【【' 结论： 从数据看，网站用户数由1号的10人增长到2号的20人，再到3号的30人，呈现出明显的上升趋势。这表明在这段时间内网站用户吸引力增强，可能与推广活动、内容更新或其他外部因素有关。";
            messages.add(SparkMessage.systemContent(predefinedInformation + "\n" + "----------------------------------"));
        }
        messages.add(SparkMessage.userContent(content));
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 模型回答的tokens的最大长度,非必传,取值为[1,4096],默认为2048
                .maxTokens(2048)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.6)
                // 指定请求版本
                .apiVersion(SparkApiVersion.V4_0)
                .build();
        // 同步调用
        String responseContent = sparkClient.chatSync(sparkRequest).getContent().trim();
        if (!isNeedTemplate) {
            return responseContent;
        }
        log.info("星火 AI 返回的结果 {}", responseContent);
        AtomicInteger atomicInteger = new AtomicInteger(1);
        while (responseContent.split("'【【【【'").length < 3) {
            responseContent = sparkClient.chatSync(sparkRequest).getContent().trim();
            if (atomicInteger.incrementAndGet() >= 4) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "星火 AI 生成失败");
            }
        }
        return responseContent;
    }


    /**
     * 向 AI 发送请求
     *
     * @param modelId 是否使用模板，进行 AI 生成； true 使用 、false 不使用 ，false 的情况是只想用 AI 不只是生成前端代码
     * @param content        内容
     *                       分析需求：
     *                       分析网站用户的增长情况
     *                       原始数据：
     *                       日期,用户数
     *                       1号,10
     *                       2号,20
     *                       3号,30
     * @return AI 返回的内容
     * '【【【【'
     * <p>
     * '【【【【'
     */
    public String sendMsgToDeepSeek(String modelId, String content) {
        List<ChatMessage> messages = new ArrayList<>();
        ArkService arkService = ArkService.builder().apiKey(apiKey)
                .build();

        // AI 生成问题的预设条件
        String predefinedInformation = "请严格按照下面的输出格式生成结果，且不得添加任何多余内容（例如无关文字、注释、代码块标记或反引号）：\n" +
                "\n" +
                "'【【【【'" +
                "{ 生成 Echarts V5 的 option 配置对象 JSON 代码，要求为合法 JSON 格式且不含任何额外内容（如注释或多余字符） } '【【【【' 结论： {\n" +
                "提供对数据的详细分析结论，内容应尽可能准确、详细，不允许添加其他无关文字或注释 }\n" +
                "\n" +
                "示例： 输入： 分析需求： 分析网站用户增长情况，请使用柱状图展示 原始数据： 日期,用户数 1号,10 2号,20 3号,30\n" +
                "\n" +
                "期望输出： '【【【【' { \"title\": { \"text\": \"分析网站用户增长情况\" }, \"xAxis\": { \"type\": \"category\", \"data\": [\"1号\", \"2号\", \"3号\"] }, \"yAxis\": { \"type\": \"value\" }, \"series\": [ { \"name\": \"用户数\", \"type\": \"bar\", \"data\": [10, 20, 30] } ] } '【【【【' 结论： 数据分析结果表明：用户规模呈现稳定的线性增长态势，日均净增用户达10人；用户留存率从首日的45%显著提升至第三日的78%，增幅达33个百分点，反映出用户粘性持续增强。值得注意的是，次日留存率单日增幅达17个百分点，显著高于同期用户规模增长率。建议结合当日的运营活动策略（如促销政策、产品功能迭代或市场推广方案）进行归因分析，以明确留存率跃升的关键驱动因素，为后续用户增长策略提供数据支撑。";
        messages.add(ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(predefinedInformation + "\n" + "----------------------------------").build());
        messages.add(ChatMessage.builder().role(ChatMessageRole.USER).content(content).build());

        // 创建聊天完成请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(modelId)// 需要替换为模型的Model ID
                .messages(messages) // 设置消息列表
                .build();
        // 同步调用
//        String responseContent = sparkClient.chatSync(sparkRequest).getContent().trim();
//        log.info("星火 AI 返回的结果 {}", responseContent);
//        AtomicInteger atomicInteger = new AtomicInteger(1);
//        while (responseContent.split("'【【【【'").length < 3) {
//            responseContent = sparkClient.chatSync(sparkRequest).getContent().trim();
//            if (atomicInteger.incrementAndGet() >= 4) {
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "星火 AI 生成失败");
//            }
//        }
        // 发送聊天完成请求并打印响应
        StringBuilder responseContent = new StringBuilder();
        try {
            // 获取响应并打印每个选择的消息内容
            arkService.streamChatCompletion(chatCompletionRequest)
                    .doOnError(Throwable::printStackTrace)
                    .blockingForEach(choice -> {
                        if (choice.getChoices().size() > 0) {
                            ChatMessage message = choice.getChoices().get(0).getMessage();
                            // 判断是否触发深度思考，触发则打印模型输出的思维链内容
                            if (message.getReasoningContent()!= null &&!message.getReasoningContent().isEmpty()) {
                                responseContent.append(message.getReasoningContent());
                            }
                            // 打印模型输出的回答内容
                            responseContent.append(message.getContent());
                        }
                    });
        } catch (Exception e) {
            responseContent.append("请求失败: ").append(e.getMessage());
        } finally {
            arkService.shutdownExecutor();
        }
        return responseContent.toString();
    }
}


