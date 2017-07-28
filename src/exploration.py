from src.normalizer import Normalizer

if __name__ == "__main__":

    text = " برای هر سهم دو خروجی در داده های تست مدنظر می باشد و هر سهم ۲۱۹ نمونه زمانی دارد. خروجی مربوط به هر سهم در دادگان تست را به ترتیب شماره سهم زیر یکدیگر قرار داده تا به یک ماتریس دو ستون و ۲۱۹۰۰ سطری رسیده شود، سپس این ماتریس را فایل csv زیر قرار دهید:"


    normalizer = Normalizer()

    normal = normalizer.normalize(text=text)

    print(normal)