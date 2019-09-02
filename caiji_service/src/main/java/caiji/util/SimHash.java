package caiji.util;

import lombok.Getter;

import java.math.BigInteger;
import java.util.StringTokenizer;
public class SimHash
{
    private String tokens;
    @Getter
    private BigInteger strSimHash;
    private int hashbits = 128;

    public SimHash(BigInteger strSimHash)
    {
        this.strSimHash = strSimHash;
    }
    public SimHash(String tokens)
    {
        this.tokens = tokens;
        this.strSimHash = this.simHash();
    }
    public SimHash(String tokens, int hashbits)
    {
        this.tokens = tokens;
        this.hashbits = hashbits;
        this.strSimHash = this.simHash();
    }
    public BigInteger simHash()
    {
        int[] v = new int[this.hashbits];
        StringTokenizer stringTokens = new StringTokenizer(this.tokens);
        while (stringTokens.hasMoreTokens())
        {
            String temp = stringTokens.nextToken();
            BigInteger t = this.hash(temp);
            for (int i = 0; i < this.hashbits; i++)
            {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                if (t.and(bitmask).signum() != 0)
                {
                    v[i] += 1;
                }
                else
                {
                    v[i] -= 1;
                }
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < this.hashbits; i++)
        {
            if (v[i] >= 0)
            {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }
        return fingerprint;
    }
    private BigInteger hash(String source)
    {
        if (source == null || source.length() == 0)
        {
            return new BigInteger("0");
        }
        else
        {
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(
                    new BigInteger("1"));
            for (char item : sourceArray)
            {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1")))
            {
                x = new BigInteger("-2");
            }
            return x;
        }
    }
    public int hammingDistance(SimHash other)
    {
        BigInteger m = new BigInteger("1").shiftLeft(this.hashbits).subtract(
                new BigInteger("1"));
        BigInteger x = this.strSimHash.xor(other.strSimHash).and(m);
        int tot = 0;
        while (x.signum() != 0)
        {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("1")));
        }
        return tot;
    }
    public static void main(String[] args)
    {
        String s = "持股过节，迎接猪年第一次井喷";
        SimHash hash1 = new SimHash(s, 128);
        System.out.println(hash1.strSimHash + "  " + hash1.strSimHash.bitLength());
        s = "中期涨跌节奏";
        SimHash hash2 = new SimHash(s, 128);
        System.out.println(hash2.strSimHash + "  " + hash2.strSimHash.bitCount());
        s = "I have a text file which contains data seperated by '|'. I need to get each field(seperated by '|') and process it. The text file can be shown as below";
        SimHash hash3 = new SimHash(s, 128);
        System.out.println(hash3.strSimHash + "  " + hash3.strSimHash.bitCount());
        System.out.println("============================");
        System.out.println(hash1.hammingDistance(hash2));
        System.out.println(hash2.hammingDistance(hash3));

        BigInteger b = new BigInteger("38700511103962416068486618");
        BigInteger c = new BigInteger("19357695684285271340017146");
        SimHash simHash4 = new SimHash(b);
        SimHash simHash5 = new SimHash(c);
        System.out.println(simHash4.hammingDistance(simHash5));
    }
}
