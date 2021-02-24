package top.topsea.calculator

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import top.topsea.calculator.database.Record
import top.topsea.calculator.database.RecordDao
import top.topsea.calculator.database.RecordDatabase


class MainActivity : AppCompatActivity() {

    private lateinit var formula: StringBuilder
    private var textsId = Array(5) { IntArray(4) }
    private val texts = Array(5) {
        arrayOfNulls<TextView>(4)
    }

    private lateinit var formulaText: TextView
    private lateinit var resultText: TextView
    private lateinit var recordsText:TextView
    private var done:Boolean = false

    private lateinit var database: RecordDatabase
    private lateinit var recordDao: RecordDao
    private lateinit var record: Record
    private var howManyRecords: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        formulaText = findViewById<TextView>(R.id.formula)
        resultText = findViewById<TextView>(R.id.result)
        recordsText = findViewById<TextView>(R.id.records_text)
        resultText.text = ""

        //数据库
        database = RecordDatabase.getInstance(this)!!
        recordDao = database.recordDao()
        val records: List<String> = recordDao.getAllRecord()
        iniRecords(records)
        record = Record(0, "", "")
        howManyRecords = records.size

        //隐藏菜单栏
//        supportActionBar?.hide()
//        supportActionBar?.title = ""

        formula = java.lang.StringBuilder()

        textsId = arrayOf(
            intArrayOf(R.id.text1, R.id.text2, R.id.text3, R.id.text4),
            intArrayOf(R.id.text5, R.id.text6, R.id.text7, R.id.text8),
            intArrayOf(R.id.text9, R.id.text10, R.id.text11, R.id.text12),
            intArrayOf(R.id.text13, R.id.text14, R.id.text15, R.id.text16),
            intArrayOf(R.id.text17, R.id.text18, R.id.text19, R.id.text20)
        )

        for (i in textsId.indices) {
            for (j in textsId[0].indices) {

                texts[i][j] = findViewById<TextView>(textsId.get(i).get(j))

                when (texts[i][j]?.let { setLabel(it.text as String) }) {
                    "num" -> {
                        texts[i][j]?.setOnClickListener { v: View? ->
                            run {
                                if (done) {
                                    formula.replace(0, formula.length, "")
                                    done = false
                                }
                                formula.append(texts[i][j]?.text)
                                printFormula(true)
                            }
                        }
                    }
                    "clear" -> {
                        texts[i][j]?.setOnClickListener { v: View? ->
                            run {
                                formula.replace(0, formula.length, "")
                                printFormula(false)
                            }
                        }
                    }
                    "delete" -> {
                        texts[i][j]?.setOnClickListener { v: View? ->
                            run {
                                formula.replace(
                                    formula.length - 1,
                                    formula.length,
                                    ""
                                )
                                printFormula(false)
                            }
                        }
                    }
                    "symbol" -> {
                        texts[i][j]?.setOnClickListener { v: View? ->
                            run {
                                formula.append(texts[i][j]?.text)
                                printFormula(false)
                            }
                        }
                    }
                    "calculation" -> {
                        texts[i][j]?.setOnClickListener { v: View? ->
                            run {
                                val result: String =
                                    SuffixCalculator.Calculating(formula.toString()) as String

                                val c: Calendar = Calendar.getInstance()
                                val mYear: Int = c.get(Calendar.YEAR)
                                val mMonth: Int = c.get(Calendar.MONTH)
                                val mDay: Int = c.get(Calendar.DAY_OF_MONTH)
                                val time = "$mYear-$mMonth-$mDay"
                                formula.append(texts[i][j]?.text)
                                formula.append(result)
                                record.formula = formula.toString()
                                record.time = time
                                record.recordId = howManyRecords % 50
                                if (howManyRecords >= 50){
                                    recordDao.updateRecord(record)
                                }else{
                                    recordDao.insertRecord(record)
                                }
                                val recordS: List<String> = recordDao.getAllRecord()
                                iniRecords(recordS)
                                done = true
                                howManyRecords++
                                printFormula(false)
                            }
                        }
                    }
                    "more" -> {

                    }
                    else -> {

                    }
                }
            }
        }

    }

    private fun iniRecords(records: List<String>){
        records.forEach {
            recordsText.append(it + "\n")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun printFormula(count: Boolean) {
        formulaText.text = formula.toString()
        if (count){
            resultText.text = "=" + SuffixCalculator.Calculating(formula.toString())
        }
    }

    private fun setLabel(text: String): String {
        val numPattern = "[0-9]+".toRegex()
        if (text.matches(numPattern)) {
            return "num"
        }
        return when (text) {
            "C" -> "clear"
            "Del" -> "delete"
            "." -> "symbol"
            "+" -> "symbol"
            "-" -> "symbol"
            "*" -> "symbol"
            "/" -> "symbol"
            "%" -> "symbol"
            "=" -> "calculation"
            "..." -> "more"
            else -> "remainder"
        }
    }
}